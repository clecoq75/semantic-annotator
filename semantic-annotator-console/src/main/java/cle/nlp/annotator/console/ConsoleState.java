package cle.nlp.annotator.console;

import cle.nlp.SupportedLanguages;
import cle.nlp.tagger.exceptions.TaggerNotFoundException;
import org.apache.commons.lang3.StringUtils;

import static cle.utils.Ansi.red;

public class ConsoleState {
    private static final String ERROR = "ERROR: ";
    public static final SupportedLanguages DEFAULT_LANGUAGE = SupportedLanguages.FR;
    private SupportedLanguages language;
    private String currentTagger = "";
    private ConsoleApp console;

    public ConsoleState(SupportedLanguages language, String currentTagger, ConsoleApp console) {
        if (console==null) {
            throw new IllegalArgumentException("Console can't be null");
        }
        this.console = console;
        this.language = language!=null? language : DEFAULT_LANGUAGE;
        setCurrentTagger(currentTagger);
    }

    public SupportedLanguages getLanguage() {
        return language;
    }

    public void setLanguage(SupportedLanguages language) {
        this.language = language!=null? language : DEFAULT_LANGUAGE;
        console.loadAllTaggers();
    }

    public void setLanguage(String lang) {
        if (StringUtils.isBlank(lang)) {
            setLanguage((SupportedLanguages)null);
        }
        else {
            SupportedLanguages sl = SupportedLanguages.getLanguage(lang);
            if (sl == null) {
                console.println(red(ERROR,true) + red("Unknown language : " + lang + "(supported languages : " + ConsoleHelp.supportedLanguageList() + ")"));
            } else {
                sl.init();
                setLanguage(sl);
            }
        }
    }

    public String getCurrentTagger() {
        return currentTagger;
    }

    public void setCurrentTagger(String tagger) {
        if (StringUtils.isBlank(tagger)) {
            this.currentTagger = "";
        }
        else {
            if (console.getSemanticAnnotator()==null) {
                console.println(red(ERROR,true) + red("Semantic annotator has errors."));
            }
            else {
                try {
                    console.getSemanticAnnotator().checkTagger(tagger);
                    currentTagger = tagger;
                } catch (TaggerNotFoundException e) {
                    console.println(red(ERROR,true) + red("Unknown tagger : " + tagger));
                }
            }
        }
    }
}
