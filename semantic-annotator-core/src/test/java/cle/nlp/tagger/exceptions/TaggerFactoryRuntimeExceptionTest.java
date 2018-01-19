package cle.nlp.tagger.exceptions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TaggerFactoryRuntimeExceptionTest {
    @Test
    public void test_constructor() {
        final String msg = "boom";
        TaggerFactoryRuntimeException e = new TaggerFactoryRuntimeException(msg);
        assertEquals(msg,e.getMessage());
    }
}
