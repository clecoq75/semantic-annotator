package cle.nlp.pattern;

import cle.nlp.partofspeech.Node;
import cle.nlp.pattern.exceptions.InvalidPatternException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class SentenceSyntacticPatternMatcherTest {

    private static final Node SENT_1 = sentence(
            node("bonjour", "I"),
            node(",", "PUNT"),
            node("le", "DET"),
            node("chien", "N"),
            node("vert", "ADJ"));

    private static final Node SENT_2 = sentence(
            node("bonjour", "I"),
            node(",", "PUNT"),
            node("le", "DET"),
            node("petit", "ADJ"),
            node("chien", "N"),
            node("vert", "ADJ"),
            node("est", "V"),
            node("dans", "ADV"),
            node("la", "DET"),
            node("lessive", "N"));

    private static final Node SENT_3 = sentence(
            node("j'", "CLS"),
            node("aime", "V"),
            node("bien", "ADV"),
            node("ce", "DET"),
            node("petit", "ADJ"),
            node("chien", "NC"),
            node("qui", "PROPEL"),
            node("chante", "V"));

    private final String expression;
    private final Node sentence;
    private final String expected;

    public SentenceSyntacticPatternMatcherTest(String expression, Node sentence, String expected) {
        this.expression = expression;
        this.sentence = sentence;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {":DET chien:", sentence(node("le", "DET"), node("chien", "N")), "(le:DET) (chien:N)"},
                {":DET chien:", SENT_1, "(le:DET) (chien:N)"},
                {":DET (:ADJ)? chien:", SENT_1, "(le:DET) (chien:N)"},
                {":DET :ADJ chien:", SENT_2, "(le:DET) (petit:ADJ) (chien:N)"},
                {"chien: (:ADJ :V) (:ADV)?", SENT_2, "(chien:N) ((vert:ADJ) (est:V)) ((dans:ADV))"},
                {"chien: (:ADJ (:V)*) (:ADV)?", SENT_2, "(chien:N) ((vert:ADJ) ((est:V))) ((dans:ADV))"},
                {"chien: ((:V)* :V) (:ADV)?", SENT_2, "not matching"},
                {"chien: (:)* lessive:", SENT_2, "(chien:N) ((vert:ADJ)) ((est:V)) ((dans:ADV)) ((la:DET)) (lessive:N)"},
                {":DET (petit:ADJ) :NC", SENT_3, "(ce:DET) ((petit:ADJ)) (chien:NC)"},
                {
                        "(:CLS être:V)? (vraiment:|trop:)? compliqué:|vague:|long: (tout: ça:)?",
                        sentence(node("c'", "CLS"),
                                node("être", "V"),
                                node("vraiment", "ADV"),
                                node("compliqué", "VPP"),
                                node("tout", "ADV"),
                                node("ça", "PRO")),
                        "((c':CLS) (être:V)) ((vraiment:ADV)) (compliqué:VPP) ((tout:ADV) (ça:PRO))"
                }
        });
    }

    private static Node node(String text, String pos) {
        return new Node(text, pos, null);
    }

    private static Node sentence(Node... nodes) {
        Node sentence = new Node("", "SENT_2", null);
        for (Node n : nodes) {
            sentence.addChild(n);
        }
        return sentence;
    }

    @Test
    public void test() throws InvalidPatternException {
        SentenceSyntacticPattern p = SentenceSyntacticPattern.getInstance(expression);
        assertEquals("Failure for : " + expression, expected, p.matcher(sentence).toString());
    }
}
