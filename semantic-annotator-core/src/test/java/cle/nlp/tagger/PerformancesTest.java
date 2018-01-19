package cle.nlp.tagger;

import cle.nlp.SupportedLanguages;
import cle.nlp.partofspeech.Node;
import cle.nlp.partofspeech.PartOfSpeechAnnotator;
import cle.nlp.tagger.exceptions.TaggerException;
import cle.nlp.tagger.models.FileSystemRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import static cle.nlp.SemanticAnnotatorUtils.readText;
import static org.junit.Assert.assertTrue;

public class PerformancesTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PerformancesTest.class);

    private TaggerFactory taggerFactory;

    public File getTaggerFolder() {
        File dir = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        return new File(dir, "semanticannotators/taggers-fr");
    }

    private void loadAllTaggers() throws FileNotFoundException {
        File taggersDir = getTaggerFolder();
        taggerFactory = new TaggerFactory(SupportedLanguages.FR, new FileSystemRepository(taggersDir));
        for (Error error : taggerFactory.getErrors()) {
            LOGGER.error("Unable to load tagger '{}' : {}", error.getTaggerName(), error.getException().getMessage());
        }
    }

    private Collection<Tag> tag(String text) {
        Collection<Tag> allTags = new HashSet<>();
        if (!StringUtils.isBlank(text)) {
            Node root = PartOfSpeechAnnotator.getInstance(SupportedLanguages.FR).annotate(text);
            Tagger tagger;
            try {
                tagger = taggerFactory.get("emotion");
                allTags = tagger.tag(root);
            } catch (TaggerException pe) {
                LOGGER.error(pe.getMessage(), pe);
            }
        }
        return allTags;
    }

    @Test
    public void test() throws IOException {
        PartOfSpeechAnnotator.getInstance(SupportedLanguages.FR);
        loadAllTaggers();

        String longText = readText("zola-au_bonheur_des_dames_CH1.txt");

        String[] text = new String[] {
                "je ne sais pas si la vie est belle mais elle vaut le coup d'être inscrite en gras !",
                "Il y avait toujours un petit chien qui chantais des chansons de David Ginola",
                "Et oui ! C'est surtout le samedi que les escargots vont à la piscine !",
                "tu m'en dira tant !",
                "où sont passé les voitures qui roullaient devant ma porte ?",
        };

        int cycle = 500;
        long mem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024L*1024L);
        LOGGER.info("Analysis of "+(cycle*text.length)+" sentences");
        LOGGER.info("Used memory before analysis   : "+mem+" MB");

        long time = System.nanoTime();
        long avg;
        for (int i=0; i<cycle; i++) {
            for (String t : text) {
                tag(t);
            }
        }
        time = System.nanoTime()-time;
        avg = time / (cycle*text.length);
        mem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024L*1024L);
        LOGGER.info("Used memory after analysis    : "+mem+" MB");
        LOGGER.info("Average duration : "+formatDuration(avg));
        LOGGER.info("Total duration   : "+formatDuration(time));

        assertTrue(avg<6000000L);

        LOGGER.info("---------");
        LOGGER.info("Analysis of the first chapter of \"Au Bonheur des Dames\"");
        mem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024L*1024L);
        LOGGER.info("Used memory before analysis   : "+mem+" MB");

        time = System.nanoTime();
        Collection<Tag> result = tag(longText);
        time = System.nanoTime()-time;
        mem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024L*1024L);
        LOGGER.info("Used mem after analysis       : "+mem+" MB");
        LOGGER.info("Duration : "+formatDuration(time));
        StringBuilder t = new StringBuilder();
        for (Tag tag : result) {
            t.append("\n  ").append(tag.toString());
        }
        LOGGER.info("Tags : {}", t.toString());

        assertTrue(time<6000000000L);
    }

    private static String formatDuration(long duration) {
        int ms = (int)(duration / 1000000L);
        long sec = ms / 1000L;
        long mili = ms % 1000L;

        StringBuilder result = new StringBuilder();

        if (sec>0) {
            result.append(sec).append("s").append(' ');
        }
        result.append(mili).append("ms");
        return result.toString();
    }
}
