package cle.nlp.morphology.fr;

import cle.nlp.morphology.MorphologicalProperties;

public class FrenchMorphologicalProperties extends MorphologicalProperties {

    public FrenchMorphologicalProperties(String lemma, String tag) {
        super(lemma, null, null, null, null, null);
        if (tag!=null) {
            updateFromTag(tag);
        }
        updateProperties();
    }

    public FrenchMorphologicalProperties(String lemma, String pers, String gender, String nb, String tps, String mode) {
        super(lemma, pers, gender, nb, tps, mode);
    }

    private void updateFromTag(String tag) {
        char[] chars = tag.toCharArray();
        for (char aChar : chars) {
            if ("123".indexOf(aChar) > -1) {
                setPersValue("" + aChar);
            } else if ("mf".indexOf(aChar) > -1) {
                setGenderValue("" + aChar);
            } else if ("ps".indexOf(aChar) > -1) {
                setNbValue("" + aChar);
            } else if ('P' == aChar) {
                setTpsValue(TPS_PRES);
                setModeValue(MODE_IND);
            } else if ('F' == aChar) {
                setTpsValue(TPS_FUT);
                setModeValue(MODE_IND);
            } else if ("IJ".indexOf(aChar) > -1) {
                setTpsValue(TPS_PAST);
                setModeValue(MODE_IND);
            } else if ('C' == aChar) {
                setTpsValue(TPS_PRES);
                setModeValue(MODE_COND);
            } else if ('Y' == aChar) {
                setTpsValue(TPS_PRES);
                setModeValue(MODE_IMP);
            } else if ('S' == aChar) {
                setTpsValue(TPS_PRES);
                setModeValue(MODE_SUBJ);
            } else if ('T' == aChar) {
                setTpsValue(TPS_PAST);
                setModeValue(MODE_SUBJ);
            } else if ('K' == aChar) {
                setTpsValue(TPS_PAST);
                setModeValue(MODE_PART);
            } else if ('G' == aChar) {
                setTpsValue(TPS_PRES);
                setModeValue(MODE_PART);
            } else if ('W' == aChar) {
                setTpsValue(TPS_PRES);
                setModeValue(MODE_INF);
            }
        }
    }
}
