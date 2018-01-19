package cle.nlp.morphology;

public interface MorphologicalPropertiesSet {
    MorphologicalProperties getMorphologicalProperties(String lemma, String type, String word);
}
