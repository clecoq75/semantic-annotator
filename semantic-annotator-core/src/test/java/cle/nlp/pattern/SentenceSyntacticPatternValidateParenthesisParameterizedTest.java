package cle.nlp.pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static cle.nlp.pattern.SentenceSyntacticPattern.findClosingParenthesis;
import static cle.nlp.pattern.SentenceSyntacticPattern.validateParenthesis;
import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SentenceSyntacticPatternValidateParenthesisParameterizedTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "()", true },
                { "aaa", true },
                { "((((a b((t))))))", true },
                { "(oo)+ (1 (z)*)", true },
                { "(oo+) (1 (z*))", false },
                { "(oo*) (1 (z+))", false },
                { ")(", false },
                { "((", false }
        });
    }

    private String expression;
    private boolean result;

    public SentenceSyntacticPatternValidateParenthesisParameterizedTest(String expression, boolean result) {
        this.expression = expression;
        this.result = result;
    }

    @Test
    public void test() {
        assertEquals("Invalid result for expression : "+expression, result, validateParenthesis(expression));
    }
}
