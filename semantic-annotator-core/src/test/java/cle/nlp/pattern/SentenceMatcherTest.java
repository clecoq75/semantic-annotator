package cle.nlp.pattern;

import cle.nlp.partofspeech.Node;
import cle.nlp.pattern.exceptions.InvalidPatternException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class SentenceMatcherTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { ":DET chien:", list(node("le", "DET"), node("chien", "N")), true },
                { ":DET (:)? chien:", list(node("le", "DET"), node("chien", "N")), true },
                { ":DET (:)* chien:", list(node("le", "DET"), node("chien", "N")), true },
                { ":DET (:)+ chien:", list(node("le", "DET"), node("petit", "ADJ"), node("chien", "N")), true },
                { ":DET (:)+ (:)? chien:", list(node("le", "DET"), node("petit", "ADJ"), node("chien", "N")), false },
                { ":DET chien: :ADJ", list(node("le", "DET"), node("chien", "N")), false },
                { ":DET chien: (:)*", list(node("le", "DET"), node("chien", "N")), true },
                { ":DET chien: (:)+", list(node("le", "DET"), node("chien", "N")), false },
                { ":DET chien: (:)", list(node("le", "DET"), node("chien", "N")), false },
                { ":DET chien:", list(node("le", "DET"), node("chien", "N"), node("vert", "ADJ")), false },

                { ":DET chien: (:)*", list(node("le", "DET"), node("chien", "N"), node("vert", "ADJ")), true },
                { ":DET chien: (:)? (:)*", list(node("le", "DET"), node("chien", "N"), node("vert", "ADJ")), true },

                { "(:DET)? chien:", list(node("le", "DET"), node("chien", "N")), true },
                { "(:DET)? chien: :ADJ", list(node("le", "DET"), node("chien", "N")), false },
                { "(:DET)? chien:", list(node("le", "DET"), node("chien", "N"), node("vert", "ADJ")), false },

                { "(:DET)? chien:", list(node("chien", "N")), true },
                { "(:DET)? chien: :ADJ", list(node("chien", "N")), false },
                { "(:DET)? chien:", list(node("chien", "N"), node("vert", "ADJ")), false },

                { ":DET (:ADJ)? chien:", list(node("le", "DET"), node("chien", "N")), true },
                { ":DET (:ADJ)? vache:", list(node("le", "DET"), node("chien", "N")), false },
                { ":DET (:ADJ)? chien:", list(node("le", "DET"), node("petit", "ADJ"), node("chien", "N")), true },

                { ":DET (:ADJ)? chien:", list(node("le", "DET"), node("bob", "N"), node("chien", "N")), false },
                { ":DET (:ADJ)? chien:", list(node("le", "DET"), node("chien", "N"), node("chien", "N")), false },

                { ":DET (:ADJ)+ chien:", list(node("le", "DET"), node("petit", "ADJ"), node("chien", "N")), true },
                { ":DET (:ADJ)+ chien:", list(node("le", "DET"), node("petit", "ADJ"), node("petit", "ADJ"), node("chien", "N")), true },
                { ":DET (:ADJ)+ chien:", list(node("le", "DET"), node("chien", "N")), false },

                { ":DET (:ADJ)* chien:", list(node("le", "DET"), node("petit", "ADJ"), node("chien", "N")), true },
                { ":DET (:ADJ)* chien:", list(node("le", "DET"), node("petit", "ADJ"), node("petit", "ADJ"), node("chien", "N")), true },
                { ":DET (:ADJ)* chien:", list(node("le", "DET"), node("chien", "N")), true },
                { ":DET (:ADJ)* chien:", list(node("le", "DET"), node("mon", "DET"), node("chien", "N")), false },

                { ":DET (chien:|vache:) :V :ADJ", list(
                        node("la", "DET"),
                        node("vache", "N"),
                        node("être", "V"),
                        node("rose", "ADJ")), true },

                { ":ADJ (chien:|vache:|bob:)?", list(
                        node("petite", "ADJ"),
                        node("vache", "N")), true },

                { ":DET (:ADJ (chien:|vache:|bob:)?) :V :ADJ", list(
                        node("la", "DET"),
                        node("petite", "ADJ"),
                        node("vache", "N"),
                        node("être", "V"),
                        node("rose", "ADJ")), true },

                { ":DET (:ADJ (chien:|vache:|bil:)?) :V :ADJ", list(
                        node("la", "DET"),
                        node("petite", "ADJ"),
                        node("être", "V"),
                        node("rose", "ADJ")), true },

                { ":DET (:ADJ (chien:|vache:|kop:)?) :V :ADJ", list(
                        node("la", "DET"),
                        node("petite", "ADJ"),
                        node("poule", "N"),
                        node("être", "V"),
                        node("rose", "ADJ")), false }
        });
    }

    private String expression;
    private List<Node> list;
    private boolean expected;

    public SentenceMatcherTest(String expression, List<Node> list, boolean result) {
        this.expression = expression;
        this.list = list;
        this.expected = result;
    }

    @Test
    public void test() throws InvalidPatternException {
        SentenceSyntacticPattern p = SentenceSyntacticPattern.getInstance(expression);
        assertEquals("Failure for : "+expression, expected, p.listMatcher(list).matches());
    }

    private static Node node(String text, String pos) {
        return new Node(text, pos, null);
    }

    private static List<Node> list(Node... nodes) {
        List<Node> result = new ArrayList<>();
        Collections.addAll(result, nodes);
        return  result;
    }
}
