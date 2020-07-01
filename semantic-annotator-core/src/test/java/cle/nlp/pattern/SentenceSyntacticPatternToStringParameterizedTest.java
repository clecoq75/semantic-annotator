package cle.nlp.pattern;

import cle.nlp.pattern.exceptions.InvalidPatternException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SentenceSyntacticPatternToStringParameterizedTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "A:", "(A@)" },
                { "(A:)", "((A@))" },
                { "(A:)+", "((A@)+)" },
                { " (A:)*", "((A@)*)" },
                { "  (A:) (B:C)*", "((A@) (B@C)*)" },
                { "A: B:C (:D)?", "(A@ B@C (@D)?)" }
        });
    }

    private final String expression;
    private final String result;

    public SentenceSyntacticPatternToStringParameterizedTest(String expression, String result) {
        this.expression = expression;
        this.result = result;
    }

    @Test
    public void test() throws InvalidPatternException {
        assertEquals("Invalid result for expression : "+expression, result, SentenceSyntacticPattern.getInstance(expression).toString());
    }
}
