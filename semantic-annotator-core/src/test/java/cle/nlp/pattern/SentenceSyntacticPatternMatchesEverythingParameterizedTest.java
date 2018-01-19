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
public class SentenceSyntacticPatternMatchesEverythingParameterizedTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "", true },
                { "(:)?", true },
                { "(:)? (A:)* (:B)*", true },
                { " (:)? (A:)* (:B)+", false },
                { "(:)? (A:)* :B", false },
                { "(:B)+ (:)? (A:)*", false },
                { ":B (:)? (A:)*", false }
        });
    }

    private String expression;
    private boolean result;

    public SentenceSyntacticPatternMatchesEverythingParameterizedTest(String expression, boolean result) {
        this.expression = expression;
        this.result = result;
    }

    @Test
    public void test() throws InvalidPatternException {
        assertEquals("Invalid result for expression : "+expression, result, SentenceSyntacticPattern.getInstance(expression).matchesEverything());
    }
}
