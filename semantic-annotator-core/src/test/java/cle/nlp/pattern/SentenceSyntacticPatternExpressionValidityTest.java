package cle.nlp.pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static cle.nlp.pattern.SentenceSyntacticPattern.PATTERN;
import static cle.nlp.pattern.SentenceSyntacticPattern.isValidExpression;
import static cle.nlp.pattern.SentenceSyntacticPattern.validateParenthesis;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SentenceSyntacticPatternExpressionValidityTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "bonjour:I", true },
                { "bonjour:I :ADJ;nb=s", true },
                { "()", true },
                { "(:)", true },
                { "(:DET chien:NC de:P :DET :NC;gender=f) ( être:V :ADJ )", true },
                { "(:DET chien:NC de:P (:DET :NC;gender=f)) (être:V :ADJ)?", true },
                { "(:DET chien:NC de:P (:DET :NC;gender=f)*) (être:V :ADJ)?", true },
                { "(:DET chien:NC de:P (:DET :NC;gender=f) (être:V :ADJ)?", false },
                { "(:DET chien:NC de:P *(:DET :NC;gender=f)) (être:V :ADJ)", false },
                { "(haïr|détester|abhorrer|maudire)", true }
        });
    }

    private String expression;
    private boolean result;

    public SentenceSyntacticPatternExpressionValidityTest(String expression, boolean result) {
        this.expression = expression;
        this.result = result;
    }

    @Test
    public void test() {
        if (result) {
            assertTrue(PATTERN.matcher(expression).matches());
            assertTrue(validateParenthesis(expression));
        }
        assertEquals("Invalid result for expression : "+expression, result, isValidExpression(expression));
    }
}
