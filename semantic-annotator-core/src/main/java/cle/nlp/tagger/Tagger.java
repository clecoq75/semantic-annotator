package cle.nlp.tagger;

import cle.nlp.SupportedLanguages;
import cle.nlp.partofspeech.Node;
import cle.nlp.partofspeech.NodeUtils;
import cle.nlp.partofspeech.PartOfSpeechAnnotator;
import cle.nlp.pattern.exceptions.InvalidPatternException;
import cle.nlp.pattern.exceptions.PatternException;
import cle.nlp.tagger.exceptions.*;
import cle.nlp.tagger.models.RuleModel;
import cle.nlp.tagger.models.TagModel;
import cle.nlp.tagger.models.TaggerModel;
import cle.nlp.tagger.models.UnitTestModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static cle.nlp.tagger.Tag.toTagSet;

public class Tagger {
    private static final Set<Tag> EMPTY = Collections.unmodifiableSet(new HashSet<>());
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final String name;
    private final List<Rule> rules;
    private final List<Set<String>> ruleSamples;
    private final Set<Tag> exportedTags;
    private final List<String> importRules;
    private final List<String> collection;
    private final SupportedLanguages language;
    private List<UnitTest> unitTests;

    private TaggerFactory taggerFactory;

    static Tagger load(SupportedLanguages language, String name, InputStream in, TaggerFactory taggerFactory)
            throws IOException, InvalidPatternException, InvalidSubstitutionException {
        TaggerModel model;
        try (InputStreamReader reader=new InputStreamReader(in, StandardCharsets.UTF_8)) {
            model = OBJECT_MAPPER.readValue(reader, TaggerModel.class);
        }
        return load(language, name, model, taggerFactory);
    }

    public static Tagger load(SupportedLanguages language, String name, TaggerModel model, TaggerFactory taggerFactory)
            throws InvalidPatternException, InvalidSubstitutionException {
        List<Rule> tmpRules = new ArrayList<>();
        List<Set<String>> tmpRuleSamples = new ArrayList<>();
        Set<String> tmpExportedTags = new HashSet<>();
        for (RuleModel ruleModel : model.getRules()) {
            Rule rule = new Rule(null, ruleModel.getPattern(), ruleModel.getRegexp(), ruleModel.getSubstitutions(),
                    ruleModel.getApplyIf(), ruleModel.getApplyIfNot());
            for (TagModel tagModel : ruleModel.getTags()) {
                rule.addGeneratedTagLabel(tagModel.getValue());
                if (tagModel.isExported()) {
                    tmpExportedTags.add(tagModel.getValue());
                }
            }
            tmpRules.add(rule);
            tmpRuleSamples.add(ruleModel.getSamples());
        }
        Tagger result = new Tagger(language, name, tmpRules, tmpRuleSamples, tmpExportedTags, model.getImportRules(), model.getCollection());
        result.setTaggerFactory(taggerFactory);
        result.setUnitTest(model.getUnitTests());
        tmpRules.forEach(rule -> rule.setTagger(result));
        return result;
    }

    private Tagger(SupportedLanguages language, String name, List<Rule> tmpRules, List<Set<String>> tmpRuleSamples,
                   Set<String> tmpExportedTags, List<String> importRules, List<String> collection) {
        this.language = language;
        this.name = name;
        this.rules = tmpRules;
        this.ruleSamples = tmpRuleSamples;
        this.exportedTags = toTagSet(tmpExportedTags);
        this.importRules = importRules;
        this.collection = collection;
    }

    public void setTaggerFactory(TaggerFactory taggerFactory) {
        this.taggerFactory = taggerFactory;
    }

    public void setUnitTest(List<UnitTestModel> unitTests) {
        this.unitTests = new ArrayList<>();
        if (unitTests!=null) {
            unitTests.forEach(model -> this.unitTests.add(new UnitTest(model)));
        }
    }

    public String getName() {
        return name;
    }

    public boolean isCollectionOnly() {
        return rules.isEmpty() && !collection.isEmpty();
    }

    public void validate() throws TaggerException, PatternException {
        boolean exportTags = false;
        for (int i=0; i<rules.size(); i++) {
            if (validateRuleAndReturnExportableTags(i, rules.get(i), exportTags,ruleSamples.get(i))) {
                exportTags = true;
            }
        }

        if ((exportTags || !collection.isEmpty()) && unitTests.isEmpty()) {
            throw new NoUnitTestsValidationException("No unit test defined");
        }

        for (UnitTest unitTest : unitTests) {
            for (String verbatim : unitTest.getVerbatim()) {
                Collection<Tag> tags = tag(verbatim);
                if (!unitTest.getTags().equals(tags)) {
                    throw new UnitTestValidationException("Invalid generated tags for '"+verbatim+" : '" + tags + "' (expected : '" + unitTest.getTags() + "')");
                }
            }
        }
    }

    private boolean validateRuleAndReturnExportableTags(int ruleIndex, Rule rule, boolean actualExportTags, Set<String> samples) throws PatternException, TaggerException {
        boolean exportTags = actualExportTags;
        if (!exportTags && !rule.getGeneratedTagsLabels().isEmpty()) {
            for (String s : rule.getGeneratedTagsLabels()) {
                if (exportedTags.contains(new Tag(s))) {
                    exportTags = true;
                }
            }
        }
        if (samples.isEmpty()) {
            throw new MissingRuleValidationSamplesException("No samples provided for rule '"+rule.toString()+"'");
        }
        else {
            validateRuleForSamples(ruleIndex, rule, samples);
        }
        return exportTags;
    }

    private void validateRuleForSamples(int ruleIndex, Rule rule, Set<String> samples) throws PatternException, TaggerException {
        for (String sample : samples) {
            Node root = PartOfSpeechAnnotator.getInstance(this.language).annotate(sample);
            Set<Tag> tags = new HashSet<>(applyDependencies(root, false));
            applyPreviousRules(root, ruleIndex, tags);

            if (!hasMatches(root, rule)) {
                final String sampleTxt = "Sample '";
                final String forTxt = "' for : \n";
                if (rule.getRegExprPattern()!=null && rule.getPattern()!=null) {
                    throw new RuleValidationException(sampleTxt + sample + "' does not match the pattern '" + rule.getPattern() + "' and/or the associated regexp '" + rule.getRegExprPattern() + forTxt + NodeUtils.printNode(root));
                } else if (rule.getPattern() != null) {
                    throw new RuleValidationException(sampleTxt + sample + "' does not match the pattern '" + rule.getPattern() + forTxt + NodeUtils.printNode(root));
                } else {
                    throw new RuleValidationException(sampleTxt + sample + "' does not match the regex '" + rule.getRegExprPattern() + forTxt + NodeUtils.printNode(root));
                }
            }
        }
    }

    private void applyPreviousRules(Node root, int ruleIndex, Set<Tag> tags) throws PatternException {
        for (int j = 0; j < ruleIndex; j++) {
            Rule rule = rules.get(j);
            if (root.getChildren()!=null) {
                for (Node sentence : root.getChildren()) {
                    tags.addAll(rule.apply(sentence, tags));
                }
            }
        }
    }

    private boolean hasMatches(Node root, Rule rule) {
        boolean hasMatches = false;
        if (root.getChildren() != null) {
            for (int j = 0; !hasMatches && j < root.getChildren().size(); j++) {
                Node sentence = root.getChildren().get(j);
                hasMatches = ((rule.getRegExprPattern() == null || rule.regExprPatternMatches(sentence.getText()))
                        && (rule.getPattern() == null || rule.getPattern().matcher(sentence).matches()));
            }
        }
        else {
            hasMatches = rule.matchesEmpty();
        }
        return hasMatches;
    }

    public Collection<Tag> tag(String txt) throws TaggerException {
        return tag(txt, false);
    }

    public Collection<Tag> tag(Node root) throws TaggerException {
        return tag(root, false);
    }

    public Collection<Tag> tag(String txt, boolean allTags) throws TaggerException {
        return tag(PartOfSpeechAnnotator.getInstance(this.language).annotate(txt), allTags);
    }

    public Collection<Tag> tag(Node root, boolean allTags) throws TaggerException {
        return tag(root, true, allTags);
    }

    public Collection<Tag> tag(Node root, boolean restrainToExportedTags, boolean allTags) throws TaggerException {
        if (root.taggerHasBeenApplied(name)) {
            return EMPTY;
        }
        else {
            root.addAppliedTagger(name);
        }

        if (root.getChildren()==null || root.getChildren().isEmpty()) {
            return EMPTY;
        }
        else {
            return doTag(root, restrainToExportedTags, allTags);
        }
    }

    private Collection<Tag> doTag(Node root, boolean restrainToExportedTags, boolean allTags) throws TaggerException {
        Collection<Tag> tags = (allTags)? new ArrayList<>() : new HashSet<>();
        Collection<Tag> tagsFromList = applyList(root, allTags);
        tags.addAll(applyDependencies(root, allTags));

        for (Node sentence : root.getChildren()) {
            for (Rule rule : rules) {
                doTagSentence(sentence, rule, tags, allTags);
            }
        }

        if (restrainToExportedTags) {
            tags.retainAll(exportedTags);
        }

        tags.addAll(tagsFromList);

        return tags;
    }

    private void doTagSentence(Node sentence, Rule rule, Collection<Tag> tags, boolean allTags) throws TaggerException {
        if (allTags || (!rule.getGeneratedTagsLabels().isEmpty() && !tags.containsAll(rule.getGeneratedTags()))
                || !rule.getSubstitutions().isEmpty()) {
            try {
                tags.addAll(rule.apply(sentence, tags));
            } catch (PatternException e) {
                throw new TaggerException("Unable to tag text with tagger '"+getName()+"' : "+e.getMessage(), e);
            }
        }
    }

    private Collection<Tag> applyDependencies(Node root, boolean findAllTags) throws TaggerException {
        return applyOtherTaggers(root, importRules, true, false, findAllTags);
    }

    private Collection<Tag> applyList(Node root, boolean findAllTags) throws TaggerException {
        return applyOtherTaggers(root, collection, false, true, findAllTags);
    }

    private Collection<Tag> applyOtherTaggers(Node root, List<String> others,
                                       boolean workOnTheSameNodeInstance, boolean restrainToExportedTags, boolean findAllTags) throws TaggerException {
        Collection<Tag> allTags = findAllTags? new ArrayList<>() : new HashSet<>();
        if (taggerFactory!=null) {
            for (String dep : others) {
                Tagger tagger = taggerFactory.get(dep);
                allTags.addAll(tagger.tag(workOnTheSameNodeInstance? root : root.copy(), restrainToExportedTags, findAllTags));
            }
        }
        return allTags;
    }

    public boolean generatesTags() {
        return !exportedTags.isEmpty();
    }

    List<UnitTest> getUnitTests() {
        return unitTests;
    }
}
