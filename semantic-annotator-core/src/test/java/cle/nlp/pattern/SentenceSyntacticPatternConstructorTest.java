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
public class SentenceSyntacticPatternConstructorTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { false, "", "()" },
                { false, "@DET;pers=2|ta", "(@DET;pers=2 | ta@)"},
                { false, "toto:|titi:", "(toto@ | titi@)" },
                { false, ":DET (chien:NC de:P (:DET (:NC;gender=f)?)) (être:V :ADJ)", "(@DET (chien@NC de@P (@DET (@NC;gender=f)?)) (être@V @ADJ))" },
                { false, "(:DET)* chien:NC", "((@DET)* chien@NC)" },
                { true, "(:DET)* (:)? chien:NC ((:)? :ADJ)? (:)*", "(chien@NC)" },
                { true, "(:DET)* chien:NC ((:)? :ADJ)? (:)*", "(chien@NC)" },
                { true, "chien:NC ((:)? :ADJ)? (:)*", "(chien@NC)" },
                { true, "(:DET)* chien:NC (:)?", "(chien@NC)" }
        });
    }

    private String expression;
    private String result;
    private boolean optimize;

    public SentenceSyntacticPatternConstructorTest(boolean optimize, String expression, String result) {
        this.optimize = optimize;
        this.expression = expression;
        this.result = result;
    }

    @Test
    public void test() throws InvalidPatternException {
        assertEquals("Invalid result for expression : "+expression, result, SentenceSyntacticPattern.getInstance(expression, optimize).toString());
    }
}
