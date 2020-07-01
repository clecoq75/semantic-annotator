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
public class BooleanTokenSyntacticPatternTest {

    private static final Node NODE1 = new Node("l'arbre", "NC", new MorphologicalProperties("arbre","1","m",null, null, null));

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { NODE1, ":", true },
                { NODE1, ":|:", true },
                { NODE1, "bob: |:NPP | :NC", true },
                { NODE1, "pépé:|:;lemma=arbre", true },
                { NODE1, "pépé|mémé|toto|:;lemma=arbre", true },

                { NODE1, ":V|l'arbre:NC;gender=f", false },
                { NODE1, ":V|:;nb=8", false },
                { NODE1, "haïr|détester|abhorrer|maudire", false }
        });
    }

    private final Node node;
    private final String expression;
    private final boolean result;

    public BooleanTokenSyntacticPatternTest(Node node, String expression, boolean result) {
        this.node = node;
        this.expression = expression;
        this.result = result;
    }

    @Test
    public void test() throws InvalidPatternException {
        BooleanTokenSyntacticPattern m = new BooleanTokenSyntacticPattern(expression,null);
        assertEquals("Invalid result for expression : "+expression, result, m.matcher(node).matches());
        assertEquals("Invalid result for expression : "+expression, result, m.matcher(node, 5).matches());
    }
}
