package cle.nlp.partofspeech;

import org.junit.Test;

import static cle.TestUtils.validateConstructorNotCallable;

public class NodeUtilsTest {
    @Test
    public void test_constructor() {
        validateConstructorNotCallable(NodeUtils.class);
    }
}
