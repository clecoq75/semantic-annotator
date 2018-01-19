package cle.nlp.pattern;

import cle.nlp.pattern.exceptions.InvalidPatternException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SentenceSyntacticMatcherTest {
    @Test
    public void test_getSourcePattern() throws InvalidPatternException {
        TokenSyntacticPattern p = new TokenSyntacticPattern(":P", null);
        SentenceSyntacticMatcher m = new SentenceSyntacticMatcher(false, null, p);
        assertEquals(p, m.getSourcePattern());
    }
}
