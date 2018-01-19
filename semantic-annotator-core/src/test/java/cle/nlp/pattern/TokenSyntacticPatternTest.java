package cle.nlp.pattern;

import cle.nlp.morphology.MorphologicalProperties;
import cle.nlp.partofspeech.Node;
import cle.nlp.pattern.exceptions.InvalidPatternException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenSyntacticPatternTest {
    @Test(expected = InvalidPatternException.class)
    public void constructor_with_invalid_pattern() throws InvalidPatternException {
        new TokenSyntacticPattern("µ23+89*", null);
    }

    @Test
    public void matches() throws InvalidPatternException {
        Node n = new Node("pépé", "NPP", new MorphologicalProperties(null,"1","m",null, null, null));
        TokenSyntacticPattern p = new TokenSyntacticPattern("pépé:NPP;pers=1;gender=m", null);
        SyntacticMatcher m = p.matcher(n);
        assertTrue(m.matches());
    }

    @Test
    public void matches2() throws InvalidPatternException {
        Node n = new Node("pépé", "NPP", new MorphologicalProperties(null,"1","m",null, null, null));
        TokenSyntacticPattern p = new TokenSyntacticPattern("pépé:NPP;pers=1;gender=m", null);
        SyntacticMatcher m = p.matcher(n, 5);
        assertTrue(m.matches());
    }

    @Test
    public void matches3() throws InvalidPatternException {
        Node n = new Node("pépé", "NPP", new MorphologicalProperties(null,null,null,null, null, null));
        TokenSyntacticPattern p = new TokenSyntacticPattern("pépé:NPP;pers=1", null);
        SyntacticMatcher m = p.matcher(n);
        assertFalse(m.matches());
    }

    @Test
    public void matches4() throws InvalidPatternException {
        Node n = new Node("pépé", "NPP", null);
        TokenSyntacticPattern p = new TokenSyntacticPattern("pépé:NPP;pers=1", null);
        SyntacticMatcher m = p.matcher(n);
        assertFalse(m.matches());
    }

    @Test
    public void matches5() throws InvalidPatternException {
        Node n = new Node("pépé", "NPP", null);
        TokenSyntacticPattern p = new TokenSyntacticPattern("pépé:NPP", null);
        SyntacticMatcher m = p.matcher(n);
        assertTrue(m.matches());
    }

    @Test
    public void get_parent() throws InvalidPatternException {
        SentenceSyntacticPattern parent = SentenceSyntacticPattern.getInstance("(pépé:NPP;pers=1;gender=m)+");
        TokenSyntacticPattern p = new TokenSyntacticPattern("pépé:NPP;pers=1;gender=m", parent);
        assertEquals(parent, p.getParent());
    }
}
