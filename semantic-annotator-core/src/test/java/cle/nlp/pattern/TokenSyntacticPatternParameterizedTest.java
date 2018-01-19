package cle.nlp.pattern;

import cle.nlp.morphology.MorphologicalProperties;
import cle.nlp.partofspeech.Node;
import cle.nlp.pattern.exceptions.InvalidPatternException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TokenSyntacticPatternParameterizedTest {

    private static final Node NODE1 = new Node("pépé", "NPP", new MorphologicalProperties(null,"1","m",null, null, null));
    private static final Node NODE2 = new Node("compliqué", "VPP", new MorphologicalProperties("compliquer","1","m",null, null, null));
    private static final Node NODE3 = new Node("what's", "DET", new MorphologicalProperties(null,"1","m",null, null, null));


    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { NODE1, ":", true },
                { NODE1, ":NPP", true },
                { NODE1, "pépé:", true },
                { NODE1, "pépé", true },
                { NODE1, "pépé:NPP", true },
                { NODE1, "pépé:NPP;pers=1", true },
                { NODE1, "pépé:NPP;gender=m;pers=1", true },
                { NODE1, "pépé:;gender=m;pers=1", true },
                { NODE1, ":NPP;gender=m;pers=1", true },
                { NODE1, ":;gender=m;pers=1", true },

                { NODE1, ":V", false },
                { NODE1, "yo:", false },
                { NODE1, "yo", false },
                { NODE1, "pépé:V", false },
                { NODE1, "yo:NPP", false },
                { NODE1, "pépé:NPP;pers=2", false },
                { NODE1, "pépé:NPP;gender=f;pers=1", false },
                { NODE2, "compliqué:", true },

                { NODE3, "what's:", true }
        });
    }

    private Node node;
    private String expression;
    private boolean result;

    public TokenSyntacticPatternParameterizedTest(Node node, String expression, boolean result) {
        this.node = node;
        this.expression = expression;
        this.result = result;
    }

    @Test
    public void test() throws InvalidPatternException {
        TokenSyntacticPattern m = new TokenSyntacticPattern(expression,null);
        assertEquals("Invalid result for expression : "+expression, result, m.matcher(node).matches());
    }
}
