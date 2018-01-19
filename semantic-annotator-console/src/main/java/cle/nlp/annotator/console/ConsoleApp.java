package cle.nlp.annotator.console;

import cle.nlp.annotator.SemanticAnnotator;

import java.io.File;
import java.io.IOException;

public interface ConsoleApp {
    void println(String text);
    void println();
    void print(String text);
    void loadAllTaggers();
    void readLine() throws IOException;
    File getAnnotatorDirectory();
    SemanticAnnotator getSemanticAnnotator();
    ConsoleState getState();
}
