package cle.nlp.annotator.console;

import cle.FileSystemTestUtils;
import cle.nlp.annotator.exceptions.SemanticAnnotatorException;
import cle.nlp.tagger.exceptions.TaggerNotFoundException;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static cle.TestUtils.validateConstructorNotCallable;
import static org.junit.Assert.assertTrue;

public class ConsoleTagTest extends ConsoleTestBase {
    @Test
    public void test1() throws FileNotFoundException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        ConsoleTag.tag("ce petit chien", null, console, false);
        assertTrue(console.getOutput().contains("smallDog"));
    }

    @Test
    public void test2() throws FileNotFoundException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        ConsoleTag.tag("ce petit chien", "smallDog", console, false);
        assertTrue(console.getOutput().contains("smallDog"));
    }

    @Test
    public void test3() throws FileNotFoundException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        ConsoleTag.tag("ce petit chien", "smallDog", console, true);
        assertTrue(console.getOutput().contains("smallDog"));
    }

    @Test
    public void test_blank() throws FileNotFoundException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        ConsoleTag.tag("", "smallDog", console, true);
        assertTrue(console.getOutput().contains("No text to tag"));
    }

    @Test
    public void test_no_annotator() throws FileNotFoundException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(false);
        ConsoleTag.tag("", "smallDog", console, true);
        assertTrue(console.getOutput().contains("Semantic annotator is not loaded"));
    }

    @Test
    public void test_file_does_not_exists() throws IOException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        ConsoleTag.tagFile("this_file_does_not_exists.exe", "smallDog", console);
        assertTrue(console.getOutput().contains("does not exists"));
    }

    @Test
    public void test_file_which_is_not_a_file() throws IOException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        ConsoleTag.tagFile(FileSystemTestUtils.getResourceFile().getParent(), "smallDog", console);
        assertTrue(console.getOutput().contains("does not exists"));
    }

    @Test
    public void test_file_with_specific_tagger() throws IOException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        ConsoleTag.tagFile(FileSystemTestUtils.getResourceFile().getAbsolutePath(), "smallDog", console);
        assertTrue(console.getOutput().contains("smallDog"));
    }

    @Test
    public void test_file() throws IOException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        ConsoleTag.tagFile(FileSystemTestUtils.getResourceFile().getAbsolutePath(), null, console);
        assertTrue(console.getOutput().contains("smallDog"));
    }

    @Test
    public void test_empty_file() throws IOException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        ConsoleTag.tagFile(FileSystemTestUtils.getResourceFile("empty.txt").getAbsolutePath(), null, console);
        assertTrue(console.getOutput().contains("No text to tag"));
    }

    @Test
    public void test_file_without_annotator() throws IOException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(false);
        ConsoleTag.tagFile(FileSystemTestUtils.getResourceFile().getAbsolutePath(), "smallDog", console);
        assertTrue(console.getOutput().contains("Semantic annotator is not loaded"));
    }

    @Test
    public void test_file_without_path() throws IOException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        ConsoleTag.tagFile(null, "smallDog", console);
        assertTrue(console.getOutput().equals(""));
    }

    @Test
    public void test_constructor() {
        validateConstructorNotCallable(ConsoleTag.class);
    }
}
