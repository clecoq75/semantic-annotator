package cle.nlp.morphology;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class MorphologicalProperties {
    public static final String LEMMA = "lemma";
    public static final String PERS = "pers";
    public static final String GENDER = "gender";
    public static final String NB = "nb";
    public static final String MODE = "mode";
    public static final String TMP = "tmp";

    protected static final String TPS_PRES = "pres";
    protected static final String TPS_FUT = "fut";
    protected static final String TPS_PAST = "past";

    protected static final String MODE_IND = "ind";
    protected static final String MODE_COND = "cond";
    protected static final String MODE_IMP = "imp";
    protected static final String MODE_SUBJ = "subj";
    protected static final String MODE_PART = "part";
    protected static final String MODE_INF = "inf";

    private String lemmaValue;
    private String persValue = null;
    private String genderValue = null;
    private String nbValue = null;
    private String modeValue = null;
    private String tpsValue = null;

    private Map<String,String> properties = new HashMap<>();

    public MorphologicalProperties(Map<String,String> properties) {
        this.properties = properties;
    }

    private MorphologicalProperties(MorphologicalProperties props) {
        this.lemmaValue = props.lemmaValue;
        this.persValue = props.persValue;
        this.genderValue = props.genderValue;
        this.nbValue = props.nbValue;
        this.tpsValue = props.tpsValue;
        this.modeValue = props.modeValue;
        if (props.properties!=null) {
            this.properties = new HashMap<>();
            this.properties.putAll(props.properties);
        }
        else {
            updateProperties();
        }
    }

    public String getLemma() {
        return lemmaValue;
    }

    public String getPers() {
        return persValue;
    }

    public String getGender() {
        return genderValue;
    }

    public String getNb() {
        return nbValue;
    }

    public String getMode() {
        return modeValue;
    }

    public String getTps() {
        return tpsValue;
    }

    protected void setLemmaValue(String lemmaValue) {
        this.lemmaValue = lemmaValue;
    }

    protected void setPersValue(String persValue) {
        this.persValue = persValue;
    }

    protected void setGenderValue(String genderValue) {
        this.genderValue = genderValue;
    }

    protected void setNbValue(String nbValue) {
        this.nbValue = nbValue;
    }

    protected void setModeValue(String modeValue) {
        this.modeValue = modeValue;
    }

    protected void setTpsValue(String tpsValue) {
        this.tpsValue = tpsValue;
    }

    public Map<String,String> asProperties() {
        return properties;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Entry<String,String> entry : properties.entrySet().stream().sorted(Comparator.comparing(Entry::getKey)).collect(Collectors.toList())) {
            if (sb.length()>0) {
                sb.append(";");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }

    public MorphologicalProperties copy() {
        return new MorphologicalProperties(this);
    }

    public MorphologicalProperties(String lemma, String pers, String gender, String nb, String tps, String mode) {
        this.lemmaValue = lemma;
        this.persValue = pers;
        this.genderValue = gender;
        this.nbValue = nb;
        this.tpsValue = tps;
        this.modeValue = mode;
        updateProperties();
    }

    protected void updateProperties() {
        updateProperty(LEMMA, lemmaValue);
        updateProperty(PERS, persValue);
        updateProperty(GENDER, genderValue);
        updateProperty(NB, nbValue);
        updateProperty(MODE, modeValue);
        updateProperty(TMP, tpsValue);
    }

    private void updateProperty(String name, String value) {
        if (value!=null) {
            properties.put(name, value);
        }
    }
}
