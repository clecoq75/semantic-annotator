package cle.nlp.morphology;

public class GenericMorphologicalPropertiesSet implements MorphologicalPropertiesSet {
    @Override
    public MorphologicalProperties getMorphologicalProperties(String lemma, String type, String word) {
        return new MorphologicalProperties(lemma,null,null,null,null,null);
    }
}
