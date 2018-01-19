package cle.nlp.annotator.console;

import cle.nlp.SupportedLanguages;

import java.util.Arrays;
import java.util.stream.Collectors;

import static cle.utils.Ansi.yellow;

public class ConsoleHelp {
    private ConsoleHelp() {
        throw new IllegalStateException("Utility class");
    }

    public static void help(int offset, ConsoleApp console) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<offset; i++) {
            sb.append(' ');
        }
        String spaces = sb.toString();
        console.println(spaces+yellow("file: <path>",true)+yellow("  Tag the content of a text file."));
        console.println(spaces+yellow("help",true)+yellow("          Print this message."));
        console.println(spaces+yellow("lang:",true)+yellow("         Change the used language ("+supportedLanguageList()+")."));
        console.println(spaces+yellow("pos: <text>",true)+yellow("   Perform the 'part of speech' tagging and morphological tagger on specified text. "));
        console.println(spaces+yellow("quit",true)+yellow("          Quit the application."));
        console.println(spaces+yellow("tag: <text>",true)+yellow("   Run all taggers on the specified text."));
        console.println(spaces+yellow("use: <tagger>",true)+yellow(" Select a tagger."));
        console.println(spaces+yellow("watch",true)+yellow("         Listen on tagger file modifications and display validation results."));
    }

    public static String supportedLanguageList() {
        return Arrays.stream(SupportedLanguages.values()).map(k -> k.name().toLowerCase()).sorted().collect(Collectors.joining(", "));
    }
}
