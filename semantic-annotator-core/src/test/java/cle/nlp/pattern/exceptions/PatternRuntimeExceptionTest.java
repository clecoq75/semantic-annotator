package cle.nlp.pattern.exceptions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PatternRuntimeExceptionTest {
    @Test
    public void test_constructor() {
        String msg = "test";
        PatternRuntimeException e = new PatternRuntimeException(msg);
        assertEquals(msg, e.getMessage());
    }
}
