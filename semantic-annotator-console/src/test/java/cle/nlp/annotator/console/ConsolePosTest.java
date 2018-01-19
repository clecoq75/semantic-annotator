package cle.nlp.annotator.console;

import cle.nlp.SupportedLanguages;
import cle.nlp.annotator.exceptions.SemanticAnnotatorException;
import org.junit.Test;

import java.io.FileNotFoundException;

import static cle.TestUtils.validateConstructorNotCallable;
import static org.junit.Assert.assertTrue;

public class ConsolePosTest extends ConsoleTestBase {
    @Test
    public void test_empty_text() throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleMock console = getConsole(false);
        ConsolePos.pos("", console, SupportedLanguages.FR);
        assertTrue(console.getOutput().contains("No text to analyse"));
    }

    @Test
    public void test() throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleMock console = getConsole(false);
        ConsolePos.pos("j'aime les fleurs", console, SupportedLanguages.FR);
        assertTrue(console.getOutput().contains("j'aime les fleurs"));
    }

    @Test
    public void test_constructor() {
        validateConstructorNotCallable(ConsolePos.class);
    }
}
