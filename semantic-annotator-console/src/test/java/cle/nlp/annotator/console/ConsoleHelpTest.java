package cle.nlp.annotator.console;

import cle.nlp.annotator.exceptions.SemanticAnnotatorException;
import cle.utils.Ansi;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;

import static cle.TestUtils.validateConstructorNotCallable;
import static org.junit.Assert.assertTrue;

public class ConsoleHelpTest extends ConsoleTestBase {
    @BeforeClass
    public static void init() {
        Ansi.setAnsiMode(false);
    }

    @Test
    public void test() throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleMock console = getConsole(false);
        ConsoleHelp.help(0, console);
        assertTrue(console.getOutput().contains("Listen on tagger file modifications and display validation results"));
    }

    @Test
    public void test_with_offset() throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleMock console = getConsole(false);
        ConsoleHelp.help(2, console);
        assertTrue(console.getOutput().contains("  watch"));
    }

    @Test
    public void test_constructor() {
        validateConstructorNotCallable(ConsoleHelp.class);
    }
}
