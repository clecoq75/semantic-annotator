package cle.nlp.pattern;

import cle.nlp.pattern.exceptions.InvalidPatternException;
import cle.nlp.pattern.exceptions.PatternException;
import org.junit.Test;

public class SentenceSyntacticPatternTest {
    @Test(expected = PatternException.class)
    public void getGroup_without_groups() throws InvalidPatternException, PatternException {
        SentenceSyntacticPattern p = SentenceSyntacticPattern.getInstance("A: b:");
        p.collectGroups();
        p.getGroup(1);
    }

    @Test(expected = PatternException.class)
    public void getGroup_with_invalid_index0() throws InvalidPatternException, PatternException {
        SentenceSyntacticPattern p = SentenceSyntacticPattern.getInstance("(A:) (b:)");
        p.collectGroups();
        p.getGroup(-1);
    }

    @Test(expected = PatternException.class)
    public void getGroup_with_invalid_index2() throws InvalidPatternException, PatternException {
        SentenceSyntacticPattern p = SentenceSyntacticPattern.getInstance("(A:) (b:)");
        p.collectGroups();
        p.getGroup(20);
    }
}
