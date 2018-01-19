package cle.nlp.annotator.console;

import cle.nlp.tagger.Tag;
import cle.nlp.tagger.exceptions.TaggerNotFoundException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static cle.utils.Ansi.*;

public final class ConsoleTag {

    public static final String ERROR_PREFIX = "ERROR: ";

    private ConsoleTag() {
        throw new IllegalStateException("Utility class");
    }

    public static void tag(String text, String currentTagger, ConsoleApp console, boolean fullScan) throws TaggerNotFoundException {
        if (!checkSemanticAnnotator(console)) {
            return;
        }

        if (!StringUtils.isBlank(text)) {
            long fullTime = System.nanoTime();
            Collection<Tag> allTags = console.getSemanticAnnotator().getTags(text, currentTagger, fullScan);
            fullTime = System.nanoTime() - fullTime;

            for (Tag tag : allTags) {
                StringBuilder sb = new StringBuilder(blue(tag.getValue()));
                sb.append("\n\t").append(yellow("text: ")).append(tag.getText());
                sb.append("\n\t").append(yellow("tagger: ")).append(tag.getTagger().getName());
                sb.append("\n\t").append(yellow("rule: ")).append(tag.getRule());
                console.println(sb.toString());
            }

            if (!fullScan) {
                console.println(yellow("---------------------------------------------------------"));
                console.println(yellow("ALL TAGS : ")
                        + blue(toStringSet(allTags).toString(), true)
                        + yellow(" (" + (fullTime / 1000000L) + "ms)"));
            }
        } else {
            console.println(yellow("No text to tag ..."));
        }
    }

    public static void tagFile(String path, String currentTagger, ConsoleApp console) throws IOException, TaggerNotFoundException {
        if (StringUtils.isBlank(path)) {
            return;
        }

        File f = new File(path);
        if (!f.exists() || !f.isFile()) {
            console.println(red(ERROR_PREFIX, true) + red("File '" + path + "' does not exists."));
            return;
        }

        String text = FileUtils.readFileToString(f, "utf-8");

        if (!StringUtils.isBlank(text)) {
            tag(text, currentTagger, console, true);
        } else {
            console.println(yellow("No text to tag ..."));
        }
    }

    private static Set<String> toStringSet(Collection<Tag> tags) {
        return tags.stream().map(Tag::getValue).collect(Collectors.toSet());
    }

    private static boolean checkSemanticAnnotator(ConsoleApp console) {
        if (console.getSemanticAnnotator()==null) {
            console.println(red(ERROR_PREFIX, true) + red("Semantic annotator is not loaded."));
            return false;
        }
        return true;
    }

}
