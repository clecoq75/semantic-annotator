package cle.nlp.annotator.console;

import cle.io.filewatcher.BasicFileWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static cle.utils.Ansi.red;
import static cle.utils.Ansi.yellow;

public final class ConsoleWatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleWatcher.class);

    private ConsoleWatcher() {
        throw new IllegalStateException("Utility class");
    }

    public static void watch(ConsoleApp console) {
        File dir = console.getAnnotatorDirectory();
        if (dir!=null) {
            try (BasicFileWatcher ignored = new BasicFileWatcher(Paths.get(dir.getAbsolutePath()),
                fileSet -> console.loadAllTaggers(), 100L)) {
                console.println(yellow("Start listening for taggers modifications (press ENTER to abort)."));

                console.readLine();
                console.println(yellow("Stop listening for taggers modifications."));
            } catch (IOException e) {
                console.println(red("An error occurred : "+e.getMessage()+"."));
                LOGGER.error(e.getMessage(), e);
            }
        }
        else {
            console.println(red("No tagger directory defined."));
        }
    }
}
