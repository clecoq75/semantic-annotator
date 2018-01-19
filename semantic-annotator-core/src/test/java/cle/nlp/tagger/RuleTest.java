package cle.nlp.tagger;

import cle.nlp.pattern.exceptions.InvalidPatternException;
import cle.nlp.tagger.exceptions.InvalidSubstitutionException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RuleTest {
    @Test
    public void test_addGeneratedTagLabel_with_null() throws InvalidPatternException, InvalidSubstitutionException {
        Rule r = new Rule(null, ":DET (petit:ADJ) :NC", null, "1:SMALL", "smallDog");
        r.addGeneratedTagLabel(null);
        assertEquals(1, r.getGeneratedTagsLabels().size());
    }

    @Test
    public void test_addGeneratedTagLabel_with_empty() throws InvalidPatternException, InvalidSubstitutionException {
        Rule r = new Rule(null, ":DET (petit:ADJ) :NC", null, "1:SMALL", "smallDog");
        r.addGeneratedTagLabel("  \t");
        assertEquals(1, r.getGeneratedTagsLabels().size());
    }

    @Test
    public void test_addGeneratedTagLabel() throws InvalidPatternException, InvalidSubstitutionException {
        Rule r = new Rule(null, ":DET (petit:ADJ) :NC", null, "1:SMALL", null);
        r.addGeneratedTagLabel("titi");
        assertEquals(1, r.getGeneratedTagsLabels().size());
        assertEquals("titi", r.getGeneratedTagsLabels().iterator().next());
    }

    @Test
    public void test_regExprPatternMatches_with_null_regex() throws InvalidPatternException, InvalidSubstitutionException {
        Rule r = new Rule(null, ":DET (petit:ADJ) :NC", null, "1:SMALL", null);
        assertFalse(r.regExprPatternMatches("toto"));
    }

    @Test
    public void test_regExprPatternMatches() throws InvalidPatternException, InvalidSubstitutionException {
        Rule r = new Rule(null, ":DET (petit:ADJ) :NC", "[abc]+", "1:SMALL", null);
        assertTrue(r.regExprPatternMatches("aab"));
    }
}
