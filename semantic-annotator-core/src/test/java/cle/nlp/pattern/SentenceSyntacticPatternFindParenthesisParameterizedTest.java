package cle.nlp.pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static cle.nlp.pattern.SentenceSyntacticPattern.findClosingParenthesis;
import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SentenceSyntacticPatternFindParenthesisParameterizedTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "()", 0, 1 },
                { "((a b(t)))", 1, 8 },
                { "((((a b((t))))))", 7, 11 },
                { "((((a b((t)", 7, -1 },
                { "((((a b((t)(((", 7, -1 }
        });
    }

    private final String expression;
    private final int start;
    private final int result;

    public SentenceSyntacticPatternFindParenthesisParameterizedTest(String expression, int start, int result) {
        this.expression = expression;
        this.start = start;
        this.result = result;
    }

    @Test
    public void test() {
        assertEquals("Invalid result for expression : "+expression, result, findClosingParenthesis(expression, start));
    }
}
