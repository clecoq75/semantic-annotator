package cle.nlp.annotator.console;

import cle.FileSystemTestUtils;
import cle.nlp.SupportedLanguages;
import cle.nlp.annotator.exceptions.SemanticAnnotatorException;
import cle.nlp.tagger.exceptions.TaggerNotFoundException;
import org.junit.Test;

import java.io.IOException;

import static cle.TestUtils.validateConstructorNotCallable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConsoleCommandTest extends ConsoleTestBase {
    @Test
    public void test_constructor() {
        validateConstructorNotCallable(ConsoleCommand.class);
    }

    @Test
    public void test_pos() throws IOException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        ConsoleCommand.executeCommand("pos: j'aime ce petit chien", console);
        assertTrue(console.getOutput().contains("SENT"));
    }

    @Test
    public void test_tag() throws IOException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        ConsoleCommand.executeCommand("tag: j'aime ce petit chien", console);
        assertTrue(console.getOutput().contains("smallDog"));
    }

    @Test
    public void test_lang() throws IOException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        ConsoleCommand.executeCommand("lang: en", console);
        assertEquals(SupportedLanguages.EN, console.getState().getLanguage());
        ConsoleCommand.executeCommand("lang: fr", console);
        assertEquals(SupportedLanguages.FR, console.getState().getLanguage());
        ConsoleCommand.executeCommand("lang: ù%", console);
        assertTrue(console.getOutput().contains("Unknown language"));
        assertEquals(SupportedLanguages.FR, console.getState().getLanguage());
    }

    @Test
    public void test_file() throws IOException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        ConsoleCommand.executeCommand("file: "+ FileSystemTestUtils.getResourceFile().getAbsolutePath(), console);
        assertTrue(console.getOutput().contains("smallDog"));
        ConsoleCommand.executeCommand("file: this_file_does_not_exists.exe", console);
        assertTrue(console.getOutput().contains("does not exists"));
    }

    @Test
    public void test_use() throws IOException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        ConsoleCommand.executeCommand("use: smallDog", console);
        assertEquals("smallDog", console.getState().getCurrentTagger());
        ConsoleCommand.executeCommand("use:", console);
        assertEquals("", console.getState().getCurrentTagger());
        ConsoleCommand.executeCommand("use: notExists", console);
        assertTrue(console.getOutput().contains("Unknown tagger : notExists"));
        assertEquals("", console.getState().getCurrentTagger());
    }

    @Test
    public void test_reload() throws IOException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        ConsoleCommand.executeCommand("reload", console);
        assertTrue(console.hasBeenReloaded());
    }

    @Test
    public void test_help() throws IOException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        ConsoleCommand.executeCommand("help", console);
        assertTrue(console.getOutput().contains("Listen on tagger file modifications and display validation results"));
    }

    @Test
    public void test_watch() throws IOException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        console.setWaitOnRead(0L);
        ConsoleCommand.executeCommand("watch", console);
        assertTrue(console.getOutput().contains("Stop listening "));
    }

    @Test
    public void test_quit() throws IOException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        assertEquals("quit", ConsoleCommand.executeCommand("quit", console));
    }

    @Test
    public void test_error() throws IOException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        ConsoleCommand.executeCommand("duchesseMaladive", console);
        assertTrue(console.getOutput().contains("Unknown command "));
    }

    @Test
    public void test_blanck() throws IOException, SemanticAnnotatorException, TaggerNotFoundException {
        ConsoleMock console = getConsole(true);
        ConsoleCommand.executeCommand("", console);
        assertEquals(0, console.getOutput().length());
    }
}
