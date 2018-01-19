package cle.nlp.annotator.console;

import cle.nlp.SupportedLanguages;
import cle.nlp.annotator.exceptions.SemanticAnnotatorException;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class ConsoleSemanticAnnotatorManagerTest extends ConsoleTestBase {
    @Test
    public void test() throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleMock console = getConsole(false);
        ConsoleSemanticAnnotatorManager manager = new ConsoleSemanticAnnotatorManager(console,
                getTaggerFolder("taggers-fr").getParentFile());
        manager.load();
        assertNotNull(manager.getSemanticAnnotator());
    }

    @Test
    public void test_no_folder() throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleMock console = getConsole(false);
        ConsoleSemanticAnnotatorManager manager = new ConsoleSemanticAnnotatorManager(console,
                getTaggerFolder("taggers-fr").getParentFile());
        console.getState().setLanguage(SupportedLanguages.EN);
        manager.load();
        assertNull(manager.getSemanticAnnotator());
    }

    @Test
    public void test_invalid_folder() throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleMock console = getConsole(false);
        ConsoleSemanticAnnotatorManager manager = new ConsoleSemanticAnnotatorManager(console,
                getTaggerFolder("taggers-fr").getParentFile());
        console.getState().setLanguage(SupportedLanguages.AR);
        manager.load();
        assertNull(manager.getSemanticAnnotator());
    }

    @Test
    public void test_with_errors() throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleMock console = getConsole(false);
        File root = new File(getTaggerFolder("taggers-fr").getParentFile().getParentFile(), "semanticannotators_errors");
        ConsoleSemanticAnnotatorManager manager = new ConsoleSemanticAnnotatorManager(console,root);
        manager.load();
        assertTrue("Invalid output : "+console.getOutput(), console.getOutput().contains("Invalid sentence expression"));
        assertNull(manager.getSemanticAnnotator());
    }
}
