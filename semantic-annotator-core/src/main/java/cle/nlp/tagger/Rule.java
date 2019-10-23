package cle.nlp.tagger;

import cle.nlp.partofspeech.Node;
import cle.nlp.pattern.SentenceSyntacticPattern;
import cle.nlp.pattern.SyntacticMatcher;
import cle.nlp.pattern.SyntacticPattern;
import cle.nlp.pattern.exceptions.InvalidPatternException;
import cle.nlp.pattern.exceptions.PatternException;
import cle.nlp.tagger.exceptions.InvalidSubstitutionException;
import cle.utils.regexp.CharSequenceWithTimeOut;
import cle.utils.regexp.CharSequenceWithTimeOutException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static cle.nlp.tagger.Tag.toTagSet;

public class Rule {
    private static final long REGEXP_TIMEOUT = 200L;
    private static final Set<Tag> EMPTY_TAG_SET = Collections.unmodifiableSet(new HashSet<>());
    private static final List<Substitution> EMPTY_SUBSTITUTIONS_SET = Collections.unmodifiableList(new ArrayList<>());
    private static final Logger LOGGER = LoggerFactory.getLogger(Rule.class);

    private final Pattern regExprPattern;

    private SentenceSyntacticPattern pattern;
    private List<Substitution> substitutions;
    private Tagger tagger;
    private Set<String> generatedTagLabels;
    private Set<Tag> generatedTags;
    private Set<Tag> applyIf;
    private Set<Tag> applyIfNot;

    public Rule(Tagger tagger, String pattern, String regExprPattern, String substitutions, String generatedTagLabels) throws InvalidPatternException, InvalidSubstitutionException {
        this(tagger, pattern, regExprPattern, substitutions, generatedTagLabels, null, null);
    }

    public Rule(Tagger tagger, String pattern, String regExprPattern, String substitutions, Set<String> applyIf, Set<String> applyIfNot) throws InvalidPatternException, InvalidSubstitutionException {
        this(tagger, pattern, regExprPattern, substitutions, null, applyIf, applyIfNot);
    }

    public Rule(Tagger tagger, String pattern, String regExprPattern, String substitutions, String generatedTagLabels, Set<String> applyIf, Set<String> applyIfNot) throws InvalidPatternException, InvalidSubstitutionException {
        this.regExprPattern = (StringUtils.isBlank(regExprPattern))? null : Pattern.compile(regExprPattern);
        this.substitutions = parseSubstitutionString(substitutions);
        this.pattern = (StringUtils.isBlank(pattern))? null : SentenceSyntacticPattern.getInstance(pattern, substitutions==null);
        this.generatedTagLabels = parseTagsString(generatedTagLabels);
        this.applyIf = applyIf!=null? toTagSet(applyIf) : new HashSet<>();
        this.applyIfNot = applyIfNot!=null? toTagSet(applyIfNot) : new HashSet<>();
        this.tagger = tagger;
        if (!this.substitutions.isEmpty() && this.pattern==null) {
            LOGGER.warn("Substitition '{}' won't be applyied as no pattern is specified.", substitutions);
        }
    }

    public Set<String> getGeneratedTagsLabels() {
        return generatedTagLabels;
    }

    public Set<Tag> getGeneratedTags() {
        if (generatedTags==null) {
            generatedTags = generatedTagLabels.stream().map(Tag::new).collect(Collectors.toSet());
        }
        return generatedTags;
    }

    public void addGeneratedTagLabel(String tag) {
        if (!StringUtils.isBlank(tag)) {
            generatedTagLabels.add(tag.trim());
        }
    }

    public List<Substitution> getSubstitutions() {
        return substitutions;
    }

    public SyntacticPattern getPattern() {
        return pattern;
    }

    public String getRegExprPattern() {
        return regExprPattern!=null? regExprPattern.toString() : null;
    }

    public boolean regExprPatternMatches(String text) {
        return regExprPattern!=null && regExprPattern.matcher(new CharSequenceWithTimeOut(text, REGEXP_TIMEOUT)).matches();
    }

    public Set<Tag> apply(Node sentence, Collection<Tag> actualTags) throws PatternException {
        if ((!applyIf.isEmpty() && !actualTags.containsAll(applyIf))
                || (!applyIfNot.isEmpty() && actualTags.containsAll(applyIfNot))) {
            return EMPTY_TAG_SET;
        }

        try {
            if (regExprPattern != null && !regExprPatternMatches(sentence.getText())) {
                return EMPTY_TAG_SET;
            } else if (pattern == null) {
                return toTags(sentence.getText());
            } else {
                SyntacticMatcher matcher = pattern.matcher(sentence);
                if (matcher.matches()) {
                    applySubstitutions(matcher, sentence);
                    return toTags(sentence.getText());
                } else {
                    return EMPTY_TAG_SET;
                }
            }
        }
        catch (CharSequenceWithTimeOutException to) {
            LOGGER.warn("Timeout while executing regular expression '{}' on text {}", regExprPattern, sentence.getText());
            return EMPTY_TAG_SET;
        }
    }

    void setTagger(Tagger tagger) {
        this.tagger = tagger;
    }

    private Set<Tag> toTags(String text) {
        if (generatedTagLabels.isEmpty()) {
            return EMPTY_TAG_SET;
        }
        else {
            Set<Tag> result = new HashSet<>();
            for (String value : generatedTagLabels) {
                result.add(new Tag(value,this, tagger, text));
            }
            return result;
        }
    }

    private void applySubstitutions(SyntacticMatcher matcher, Node sentence) throws PatternException {
        if (!substitutions.isEmpty()) {
            HashMap<SentenceSyntacticPattern, Set<Node>> matchedNodesByPatterns = new HashMap<>();
            collectMatchedNodesByPatterns(matcher, matchedNodesByPatterns);
            int maxIdx = 0;
            for (Substitution substitution : substitutions) {
                SentenceSyntacticPattern group = pattern.getGroup(substitution.getGroupIndex());
                Set<Node> matchedNodes = matchedNodesByPatterns.get(group);
                if (matchedNodes != null && !matchedNodes.isEmpty()) {
                    maxIdx = this.applySubstitutions(sentence, matchedNodes, substitution, maxIdx);
                }
            }
            if (maxIdx>0 && maxIdx<sentence.getChildren().size()) {
                SyntacticMatcher followingMatcher = pattern.matcher(sentence, maxIdx+1);
                if (followingMatcher.matches()) {
                    applySubstitutions(followingMatcher, sentence);
                }
            }
        }
    }

    private int applySubstitutions(Node sentence, Set<Node> matchedNodes, Substitution substitution, int maxIdx) {
        int beginIndex = -1;
        int lastIndex = -1;
        int maxIndex = maxIdx;
        for (int i = 0; i < sentence.getChildren().size(); i++) {
            Node n = sentence.getChildren().get(i);
            if (matchedNodes.contains(n)) {
                if (beginIndex < 0) {
                    beginIndex = i;
                }
                lastIndex = i;
                if (maxIndex<lastIndex) {
                    maxIndex = lastIndex;
                }
            }
        }
        sentence.gatherChildren(substitution.getNewLabel(), beginIndex, lastIndex + 1);
        return maxIndex;
    }

    private void collectMatchedNodesByPatterns(SyntacticMatcher matcher, HashMap<SentenceSyntacticPattern, Set<Node>> matchedNodesByPatterns) {
        if (!matcher.getMatchedNodes().isEmpty()) {
            SyntacticPattern sp = matcher.getSourcePattern();
            for (Node n : matcher.getMatchedNodes()) {
                addNodeToAncestors(n,sp,matchedNodesByPatterns);
            }
        }
        if (!matcher.getSubMatcherList().isEmpty()) {
            matcher.getSubMatcherList().forEach(syntacticMatcher -> collectMatchedNodesByPatterns(syntacticMatcher,matchedNodesByPatterns));
        }
    }

    private void addNodeToAncestors(Node n, SyntacticPattern sp, HashMap<SentenceSyntacticPattern, Set<Node>> matchedNodesByPatterns) {
        if (sp.getClass().equals(SentenceSyntacticPattern.class)) {
            SentenceSyntacticPattern nlsp = (SentenceSyntacticPattern)sp;
            Set<Node> s = matchedNodesByPatterns.computeIfAbsent(nlsp, k -> new HashSet<>());
            s.add(n);
        }

        SyntacticPattern parent = sp.getParent();
        while (parent!=null) {
            addNodeToAncestors(n,parent,matchedNodesByPatterns);
            parent = parent.getParent();
        }
    }

    private Set<String> parseTagsString(String tags) {
        if (StringUtils.isBlank(tags)) {
            return new HashSet<>();
        }
        else {
            Set<String> result = new HashSet<>();
            String[] parts = tags.split("[;,]");
            for (String part : parts) {
                if (!StringUtils.isBlank(part)) {
                    result.add(part.trim());
                }
            }
            return result;
        }
    }

    private List<Substitution> parseSubstitutionString(String substitutions) throws InvalidSubstitutionException {
        if (StringUtils.isBlank(substitutions)) {
            return EMPTY_SUBSTITUTIONS_SET;
        }
        else {
            List<Substitution> result = new ArrayList<>();
            String[] parts = substitutions.split("[;,]");
            for (String part : parts) {
                parseSingleSubstitutionString(part, result);
            }
            return !result.isEmpty()? result : EMPTY_SUBSTITUTIONS_SET;
         }
    }

    private void parseSingleSubstitutionString(String part, List<Substitution> result) throws InvalidSubstitutionException {
        if (!StringUtils.isBlank(part)) {
            int idx = part.indexOf(':');
            if (idx < 0) {
                throw new InvalidSubstitutionException("Invalid syntax for : " + part);
            } else {
                String p1 = part.substring(0, idx).trim();
                String p2 = part.substring(idx + 1).trim();
                int position;
                try {
                    position = Integer.parseInt(p1);
                } catch (NumberFormatException nfe) {
                    throw new InvalidSubstitutionException("Invalid syntax for : " + part + ". Invalid group position : " + p1);
                }
                result.add(new Substitution(position, p2));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        append(sb, "pattern", pattern);
        append(sb, "regexp", regExprPattern);
        append(sb, "applyIf", applyIf);
        append(sb, "applyIfNot", applyIfNot);
        append(sb, "substitutions", substitutions);
        append(sb, "tags", generatedTagLabels);
        return sb.toString();
    }

    private void append(StringBuilder sb, String name, Object value) {
        if (value!=null) {
            if (sb.length()>0) {
                sb.append("; ");
            }
            sb.append(name).append(": ").append(value.toString());
        }
    }

    public boolean matchesEmpty() {
        return (regExprPattern==null || regExprPatternMatches(""))
                && (pattern==null || pattern.matchesEverything());
    }
}
