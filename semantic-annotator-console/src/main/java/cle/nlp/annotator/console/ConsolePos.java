package cle.nlp.annotator.console;

import cle.nlp.SupportedLanguages;
import cle.nlp.partofspeech.PartOfSpeechAnnotator;
import org.apache.commons.lang3.StringUtils;

import static cle.nlp.partofspeech.NodeUtils.printNode;
import static cle.utils.Ansi.yellow;

public class ConsolePos {
    private ConsolePos() {
        throw new IllegalStateException("Utility class");
    }

    public static void pos(String text, ConsoleApp console, SupportedLanguages language) {
        if (!StringUtils.isBlank(text)) {
            console.println(printNode(PartOfSpeechAnnotator.getInstance(language).annotate(text)).toString());
        }
        else {
            console.println(yellow("No text to analyse ..."));
        }
    }
}
