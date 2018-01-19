package cle.nlp;

import cle.nlp.morphology.MorphologicalAnalyzer;
import cle.nlp.partofspeech.PartOfSpeechAnnotator;
import org.apache.commons.lang3.StringUtils;

public enum SupportedLanguages {
    AR,
    DE,
    ES,
    ZH,
    EN,
    FR;

    public static SupportedLanguages getLanguage(String lang) {
        if (StringUtils.isBlank(lang)) {
            return null;
        }
        for (SupportedLanguages l : SupportedLanguages.values()) {
            if (l.name().equalsIgnoreCase(lang)) {
                return l;
            }
        }
        return null;
    }

    public void init() {
        PartOfSpeechAnnotator.getInstance(this);
        MorphologicalAnalyzer.getInstance(this);
    }
}
