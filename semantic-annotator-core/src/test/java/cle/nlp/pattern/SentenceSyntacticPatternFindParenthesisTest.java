package cle.nlp.pattern;

import org.junit.Test;

public class SentenceSyntacticPatternFindParenthesisTest {
    @Test(expected = IllegalArgumentException.class)
    public void null_text() {
        SentenceSyntacticPattern.findClosingParenthesis(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negative_index() {
        SentenceSyntacticPattern.findClosingParenthesis("aa (56) hju", -5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void index_out_of_bounce() {
        SentenceSyntacticPattern.findClosingParenthesis("aa (56) hju", 45);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalid_index() {
        SentenceSyntacticPattern.findClosingParenthesis("aa (56) hju", 2);
    }
}
