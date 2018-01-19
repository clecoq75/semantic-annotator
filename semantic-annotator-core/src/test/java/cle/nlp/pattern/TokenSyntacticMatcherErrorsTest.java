package cle.nlp.pattern;

import cle.nlp.partofspeech.Node;
import cle.nlp.pattern.exceptions.InvalidPatternException;
import org.junit.Test;

public class TokenSyntacticMatcherErrorsTest {
    @Test(expected = IllegalArgumentException.class)
    public void test_non_matching_with_node() {
        new TokenSyntacticMatcher(false, new Node("pok", "KI", null), 0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_non_matching_with_invalid_relevance() {
        new TokenSyntacticMatcher(false, null, 2, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_non_matching_with_source_pattern() throws InvalidPatternException {
        new TokenSyntacticMatcher(false, null, 0, new TokenSyntacticPattern("pok:KI", null));
    }

    @Test
    public void test_matching() throws InvalidPatternException {
        new TokenSyntacticMatcher(true, new Node("pok", "KI", null),
                1, new TokenSyntacticPattern("pok:KI", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_matching_with_null_node() throws InvalidPatternException {
        new TokenSyntacticMatcher(true, null,1, new TokenSyntacticPattern("pok:KI", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_matching_with_invalid_relevance() throws InvalidPatternException {
        new TokenSyntacticMatcher(true, new Node("pok", "KI", null),0, new TokenSyntacticPattern("pok:KI", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_matching_with_null_pattern() {
        new TokenSyntacticMatcher(true, new Node("pok", "KI", null),1, null);
    }
}
