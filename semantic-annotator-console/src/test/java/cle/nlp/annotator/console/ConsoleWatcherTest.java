package cle.nlp.annotator.console;

import cle.FileSystemTestUtils;
import cle.utils.Sandman;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static cle.TestUtils.validateConstructorNotCallable;
import static org.junit.Assert.assertTrue;

public class ConsoleWatcherTest extends ConsoleTestBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleWatcherTest.class);
    private static File watchedDir;

    @BeforeClass
    public static void init() throws IOException {
        watchedDir = FileSystemTestUtils.createTmpDir();
    }

    @Test
    public void test() {
        ConsoleMock console = new ConsoleMock(watchedDir,300L);
        Thread t = new Thread(() -> {
            try {
                Sandman.sleep(100L);
                LOGGER.info("Add new file");
                FileSystemTestUtils.createNewFile(watchedDir);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        });
        t.setDaemon(true);
        t.start();
        ConsoleWatcher.watch(console);

        assertTrue(console.getOutput().contains("Stop listening "));
        assertTrue(console.hasBeenReloaded());
    }

    @Test
    public void test_io_exception() {
        ConsoleMock console = new ConsoleMock(watchedDir,300L);
        console.setExceptionOnRead(true);
        ConsoleWatcher.watch(console);
        assertTrue(console.getOutput().contains("An error occurred : boom"));
    }

    @Test
    public void test_constructor() {
        validateConstructorNotCallable(ConsoleWatcher.class);
    }

    @Test
    public void test_no_dir() {
        ConsoleMock console = new ConsoleMock(null,300L);
        ConsoleWatcher.watch(console);
        assertTrue(console.getOutput().contains("No tagger directory defined"));
    }
}