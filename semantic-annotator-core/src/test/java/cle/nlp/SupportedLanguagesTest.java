package cle.nlp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SupportedLanguagesTest {
    @Test
    public void test_get_language() {
        for (SupportedLanguages l : SupportedLanguages.values()) {
            assertEquals(l, SupportedLanguages.getLanguage(l.name().toLowerCase()));
        }
    }

    @Test
    public void test_get_unknown_language() {
        assertNull(SupportedLanguages.getLanguage("%$"));
    }

    @Test
    public void test_get_empty_language() {
        assertNull(SupportedLanguages.getLanguage(""));
        assertNull(SupportedLanguages.getLanguage(null));
    }
}
