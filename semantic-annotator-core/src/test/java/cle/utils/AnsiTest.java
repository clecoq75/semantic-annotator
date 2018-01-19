package cle.utils;

import org.junit.Test;

import static cle.TestUtils.validateConstructorNotCallable;

public class AnsiTest {
    @Test
    public void test_constructor() {
        validateConstructorNotCallable(Ansi.class);
    }
}
