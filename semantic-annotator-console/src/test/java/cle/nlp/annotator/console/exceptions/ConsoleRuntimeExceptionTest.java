package cle.nlp.annotator.console.exceptions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConsoleRuntimeExceptionTest {
    @Test
    public void test_constructor() {
        String msg = "badaboom";
        Exception e = new Exception("boom");
        ConsoleRuntimeException ce = new ConsoleRuntimeException(msg, e);
        assertEquals(msg, ce.getMessage());
        assertEquals(e, ce.getCause());
    }
}
