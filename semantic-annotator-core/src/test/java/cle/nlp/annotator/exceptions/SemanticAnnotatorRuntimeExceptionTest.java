package cle.nlp.annotator.exceptions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SemanticAnnotatorRuntimeExceptionTest {
    @Test
    public void test_constructor() {
        final String msg = "boom";
        final Exception source = new Exception("badaboom");
        SemanticAnnotatorRuntimeException e = new SemanticAnnotatorRuntimeException(msg, source);
        assertEquals(msg,e.getMessage());
        assertEquals(source,e.getCause());
    }
}
