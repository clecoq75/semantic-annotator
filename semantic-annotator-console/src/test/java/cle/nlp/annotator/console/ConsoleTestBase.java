package cle.nlp.annotator.console;

import cle.nlp.SupportedLanguages;
import cle.nlp.annotator.SemanticAnnotator;
import cle.nlp.annotator.exceptions.SemanticAnnotatorException;

import java.io.File;
import java.io.FileNotFoundException;

import static cle.nlp.SupportedLanguages.FR;

public class ConsoleTestBase {

    private static SemanticAnnotator frenchAnnotator;

    public static File getTaggerFolder(String taggerName) {
        File resources = new File(ConsoleTagTest.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        return new File(resources, "semanticannotators/"+taggerName);
    }

    public static SemanticAnnotator load(String taggerName, SupportedLanguages language) throws FileNotFoundException, SemanticAnnotatorException {
        return new SemanticAnnotator(language, getTaggerFolder(taggerName).getAbsolutePath());
    }

    public static SemanticAnnotator getFrenchTagger() throws FileNotFoundException, SemanticAnnotatorException {
        if (frenchAnnotator==null) {
            frenchAnnotator = load("taggers-fr", FR);
        }
        return frenchAnnotator;
    }

    public static ConsoleMock getConsole(boolean withSemanticAnnotator) throws FileNotFoundException, SemanticAnnotatorException {
        ConsoleMock console = new ConsoleMock();
        if (withSemanticAnnotator) {
            console.setSemanticAnnotator(getFrenchTagger());
        }
        return console;
    }
}
