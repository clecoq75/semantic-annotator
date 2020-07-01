package cle.nlp.morphology;

import cle.nlp.SupportedLanguages;
import cle.nlp.morphology.fr.FrenchMorphologicalPropertiesSet;

import java.util.EnumMap;

public final class MorphologicalAnalyzer {
    private static final EnumMap<SupportedLanguages,MorphologicalAnalyzer> instances = new EnumMap<>(SupportedLanguages.class);

    private final MorphologicalPropertiesSet morphologicalPropertiesSet;

    public static MorphologicalAnalyzer getInstance(SupportedLanguages language) {
        MorphologicalAnalyzer result = instances.get(language);
        if (result==null) {
            if (language==SupportedLanguages.FR) {
                result = new MorphologicalAnalyzer(FrenchMorphologicalPropertiesSet.getInstance());
            }
            else {
                result =  new MorphologicalAnalyzer(new GenericMorphologicalPropertiesSet());
            }
            instances.put(language, result);
        }
        return result;
    }

    private MorphologicalAnalyzer(MorphologicalPropertiesSet morphologicalPropertiesSet) {
        this.morphologicalPropertiesSet = morphologicalPropertiesSet;
    }

    public MorphologicalProperties getMorphologicalProperties(String lemma, String type, String word) {
        return morphologicalPropertiesSet.getMorphologicalProperties(lemma, type, word.toLowerCase());
    }
}
