package cle.nlp.annotator.console;

import cle.nlp.annotator.SemanticAnnotator;
import cle.nlp.annotator.console.exceptions.ConsoleRuntimeException;
import cle.nlp.annotator.exceptions.SemanticAnnotatorException;
import cle.nlp.tagger.Error;

import java.io.File;
import java.io.FileNotFoundException;

import static cle.utils.Ansi.red;
import static cle.utils.Ansi.yellow;

public class ConsoleSemanticAnnotatorManager {
    private final ConsoleApp console;
    private SemanticAnnotator semanticAnnotator;
    private final File root;

    public ConsoleSemanticAnnotatorManager(ConsoleApp console, File root) {
        this.console = console;
        this.root = root;
    }

    private final Object taggerLock = new Object();

    public void load() {
        synchronized (taggerLock) {
            if (getTaggerFolder()!=null) {
                long time = System.nanoTime();
                try {
                    semanticAnnotator = new SemanticAnnotator(console.getState().getLanguage(), getTaggerFolder());
                    time = System.nanoTime() - time;
                    console.println(yellow(semanticAnnotator.getTaggersCount() + " tagger" + (semanticAnnotator.getTaggersCount() > 1 ? "s" : "") + " loaded and validated (" + (time / 1000000L) + "ms)"));
                } catch (FileNotFoundException e) {
                    semanticAnnotator = null;
                    throw new ConsoleRuntimeException("Unable to load models : " + e.getMessage(), e);
                } catch (SemanticAnnotatorException e) {
                    semanticAnnotator = null;
                    for (Error error : e.getErrors()) {
                        console.println(red("ERROR:",true) + red(" Unable to load tagger '" + error.getTaggerName() + "' : " + error.getException().getMessage()));
                    }
                }
            }
        }
    }

    public SemanticAnnotator getSemanticAnnotator() {
        return semanticAnnotator;
    }

    public File getTaggerFolder() {
        File taggerFolder = new File(root, "taggers-"+console.getState().getLanguage().name().toLowerCase());
        if (!taggerFolder.exists() || !taggerFolder.isDirectory()) {
            console.println(red("ERROR: ",true) + red("No folder '"+taggerFolder.getAbsolutePath()+"' found."));
            return null;
        }
        else {
            return taggerFolder;
        }
    }
}
