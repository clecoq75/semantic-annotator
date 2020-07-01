package cle.nlp.tagger;

import cle.nlp.SupportedLanguages;
import cle.nlp.partofspeech.Node;
import cle.nlp.partofspeech.PartOfSpeechAnnotator;
import cle.nlp.pattern.exceptions.InvalidPatternException;
import cle.nlp.pattern.exceptions.PatternException;
import cle.nlp.tagger.exceptions.InvalidSubstitutionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static cle.nlp.SemanticAnnotatorUtils.toTagSet;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class RuleParameterizedTest {

    private static final PartOfSpeechAnnotator PART_OF_SPEECH_ANNOTATOR = PartOfSpeechAnnotator.getInstance(SupportedLanguages.FR);

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws InvalidPatternException, InvalidSubstitutionException {
        return Arrays.asList(new Object[][]{
                {
                    "j'aime bien ce petit chien qui chante",
                    new Rule(null, ":DET (petit:ADJ) :NC", null, "1:SMALL", "smallDog"),
                    "START CLS V ADV DET SMALL NC PROREL V END",
                    toTagSet("smallDog")
                },
                {
                    "j'aime bien ce petit chien qui chante",
                    new Rule(null, "((:CLS)? aimer:V) bien:ADV", null, "1:LOVE", "smallDog"),
                    "START LOVE ADV DET ADJ NC PROREL V END",
                    toTagSet("smallDog")
                },
                {
                    "j'aime bien ce petit chien qui chante",
                    new Rule(null, "(:ADV) (:DET (:)* :PROREL)", null, "2:KOKO", "smallDog"),
                    "START CLS V ADV KOKO V END",
                    toTagSet("smallDog")
                },
                {
                    "j'aime bien ce petit chien qui chante",
                    new Rule(null, "(:ADV) (:DET (:)* :PROREL)", null, "1:KIKI;2:KOKO", "smallDog"),
                    "START CLS V KIKI KOKO V END",
                    toTagSet("smallDog")
                },
                {
                    "ce petit chien",
                    new Rule(null, ":DET (petit:ADJ) :NC", null, "1:SMALL", "smallDog"),
                    "START DET SMALL NC END",
                    toTagSet("smallDog")
                },
                {
                    "ce petit chien",
                    new Rule(null, "(yes:)? :DET (petit:ADJ) :NC (no:)? (:)*", null, "1:SMALL", "smallDog"),
                    "START DET ADJ NC END",
                    toTagSet("smallDog")
                },
                {
                        "ce petit chien",
                        new Rule(null, null, null, "1:SMALL", "always"),
                        "START DET ADJ NC END",
                        toTagSet("always")
                }
        });
    }

    private final String text;
    private final Rule rule;
    private final String expected;
    private final Set<Tag> tags;

    public RuleParameterizedTest(String text, Rule rule, String expected, Set<Tag> tags) {
        this.text = text;
        this.rule = rule;
        this.expected = expected;
        this.tags = tags;
    }

    @Test
    public void test() throws PatternException {
        Node n = PART_OF_SPEECH_ANNOTATOR.annotate(text);
        Node sentence = n.getChildren().get(0);
        Set<Tag> result = rule.apply(sentence, new HashSet<>());
        assertEquals(expected, onlyPOS(sentence));
        assertEquals(tags, result);
    }

    private String onlyPOS(Node n) {
        StringBuilder sb = new StringBuilder();
        for (Node c : n.getChildren()) {
            if (sb.length()>0) {
                sb.append(' ');
            }
            sb.append(c.getPartOfSpeech());
        }
        return sb.toString();
    }
}
