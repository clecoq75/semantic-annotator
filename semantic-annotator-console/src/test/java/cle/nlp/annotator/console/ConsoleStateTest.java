package cle.nlp.annotator.console;

import cle.nlp.SupportedLanguages;
import cle.nlp.annotator.exceptions.SemanticAnnotatorException;
import org.junit.Test;

import java.io.FileNotFoundException;

import static cle.nlp.annotator.console.ConsoleState.DEFAULT_LANGUAGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConsoleStateTest extends ConsoleTestBase {
    @Test
    public void test_constructor_with_null() throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleState state = new ConsoleState(null, null, getConsole(true));
        assertEquals(DEFAULT_LANGUAGE, state.getLanguage());
        assertEquals("", state.getCurrentTagger());
    }

    @Test
    public void test_constructor_with_null_language() throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleState state = new ConsoleState(null, "smallDog", getConsole(true));
        assertEquals(DEFAULT_LANGUAGE, state.getLanguage());
        assertEquals("smallDog", state.getCurrentTagger());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_with_null_console() {
        new ConsoleState(SupportedLanguages.EN, "smallDog", null);
    }

    @Test
    public void test_constructor_with_null_tagger() throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleState state = new ConsoleState(SupportedLanguages.EN, null, getConsole(true));
        assertEquals(SupportedLanguages.EN, state.getLanguage());
        assertEquals("", state.getCurrentTagger());
    }

    @Test
    public void test_constructor_with_invalid_tagger() throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleMock console = getConsole(true);
        new ConsoleState(SupportedLanguages.EN, "notExists", console);
        assertTrue(console.getOutput().contains("Unknown tagger : notExists"));
    }

    @Test
    public void test_setLanguage() throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleState state = new ConsoleState(SupportedLanguages.EN, "", getConsole(true));
        assertEquals(SupportedLanguages.EN, state.getLanguage());
        state.setLanguage(SupportedLanguages.FR);
        assertEquals(SupportedLanguages.FR, state.getLanguage());
    }

    @Test
    public void test_setLanguage_with_null() throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleState state = new ConsoleState(SupportedLanguages.EN, "", getConsole(true));
        assertEquals(SupportedLanguages.EN, state.getLanguage());
        state.setLanguage((SupportedLanguages)null);
        assertEquals(DEFAULT_LANGUAGE, state.getLanguage());
    }

    @Test
    public void test_setLanguage_with_string() throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleState state = new ConsoleState(SupportedLanguages.EN, "", getConsole(true));
        assertEquals(SupportedLanguages.EN, state.getLanguage());
        state.setLanguage("fr");
        assertEquals(SupportedLanguages.FR, state.getLanguage());
    }

    @Test
    public void test_setLanguage_with_invalid_language() throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleMock console = getConsole(true);
        ConsoleState state = new ConsoleState(SupportedLanguages.EN, "", console);
        assertEquals(SupportedLanguages.EN, state.getLanguage());
        state.setLanguage("ù%");
        assertTrue(console.getOutput().contains("Unknown language"));
    }

    @Test
    public void test_setLanguage_with_empty_language() throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleState state = new ConsoleState(SupportedLanguages.EN, "", getConsole(true));
        assertEquals(SupportedLanguages.EN, state.getLanguage());
        state.setLanguage((String)null);
        assertEquals(DEFAULT_LANGUAGE, state.getLanguage());
    }

    @Test
    public void test_setCurrentTagger() throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleMock console = getConsole(true);
        ConsoleState state = new ConsoleState(SupportedLanguages.EN, null, console);
        assertEquals("", state.getCurrentTagger());
        state.setCurrentTagger("smallDog");
        assertEquals("smallDog", state.getCurrentTagger());
    }

    @Test
    public void test_setCurrentTagger_with_null() throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleMock console = getConsole(true);
        ConsoleState state = new ConsoleState(SupportedLanguages.EN, "smallDog", console);
        assertEquals("smallDog", state.getCurrentTagger());
        state.setCurrentTagger(null);
        assertEquals("", state.getCurrentTagger());
    }

    @Test
    public void test_setCurrentTagger_with_invalid_tagger() throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleMock console = getConsole(true);
        new ConsoleState(SupportedLanguages.EN, "notExists", console);
        assertTrue(console.getOutput().contains("Unknown tagger : notExists"));
    }
}
