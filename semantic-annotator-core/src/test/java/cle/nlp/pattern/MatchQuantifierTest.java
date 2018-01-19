package cle.nlp.pattern;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MatchQuantifierTest {
    @Test
    public void test_fromChar_none_or_once() {
        assertEquals(MatchQuantifier.NONE_OR_ONCE, MatchQuantifier.fromChar('?'));
    }

    @Test
    public void test_fromChar_at_least_once() {
        assertEquals(MatchQuantifier.AT_LEAST_ONCE, MatchQuantifier.fromChar('+'));
    }

    @Test
    public void test_fromChar_any() {
        assertEquals(MatchQuantifier.ANY, MatchQuantifier.fromChar('*'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_fromChar_error() {
        assertEquals(MatchQuantifier.ANY, MatchQuantifier.fromChar('ù'));
    }
}
