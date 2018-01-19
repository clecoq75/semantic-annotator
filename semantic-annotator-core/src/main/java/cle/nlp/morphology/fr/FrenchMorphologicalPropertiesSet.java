package cle.nlp.morphology.fr;

import cle.nlp.morphology.MorphologicalProperties;
import cle.nlp.morphology.MorphologicalPropertiesSet;
import edu.stanford.nlp.io.RuntimeIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class FrenchMorphologicalPropertiesSet implements MorphologicalPropertiesSet {
    private static final Logger LOGGER = LoggerFactory.getLogger(FrenchMorphologicalPropertiesSet.class);

    private static FrenchMorphologicalPropertiesSet instance;

    HashMap<String, HashMap<String,FrenchMorphologicalProperties>> tagSet = new HashMap<>();

    private static final HashMap<String,List<String>> CORRESPONDENCES = new HashMap<>();
    static {
        CORRESPONDENCES.put("V", Collections.singletonList("v"));
        CORRESPONDENCES.put("VS", Collections.singletonList("v"));
        CORRESPONDENCES.put("VPP",Collections.singletonList("v"));
        CORRESPONDENCES.put("VINF",Collections.singletonList("v"));
        CORRESPONDENCES.put("VIMP",Collections.singletonList("v"));
        CORRESPONDENCES.put("PRO",Collections.singletonList("pro"));
        CORRESPONDENCES.put("PROREL",Collections.singletonList("prel"));
        CORRESPONDENCES.put("DET",Collections.singletonList("det"));
        CORRESPONDENCES.put("NC",Collections.singletonList("nc"));
        CORRESPONDENCES.put("N",Collections.singletonList("nc"));
        CORRESPONDENCES.put("ADJ",Collections.singletonList("adj"));
        CORRESPONDENCES.put("CLS",Arrays.asList("cln","cla","cld"));
        CORRESPONDENCES.put("CL",Collections.singletonList("cln"));
        CORRESPONDENCES.put("CLR",Collections.singletonList("clr"));
        CORRESPONDENCES.put("CLO",Collections.singletonList("cld"));
        CORRESPONDENCES.put("CC",Collections.singletonList("coo"));
        CORRESPONDENCES.put("D",Collections.singletonList("prep"));
        CORRESPONDENCES.put("ADV",Collections.singletonList("adv"));
        CORRESPONDENCES.put("P",Collections.singletonList("prep"));
        CORRESPONDENCES.put("CS",Arrays.asList("csu","prel"));
        CORRESPONDENCES.put("ADVWH",Collections.singletonList("pri"));
        CORRESPONDENCES.put("VPR",Collections.singletonList("vpr"));
        CORRESPONDENCES.put("PROWH",Collections.singletonList("pri"));
    }

    private static final HashSet<String> IGNORE_LEMMA_FOR = new HashSet<>();
    static {
        IGNORE_LEMMA_FOR.add("CLR");
        IGNORE_LEMMA_FOR.add("ADVWH");
        IGNORE_LEMMA_FOR.add("PUNC");
        IGNORE_LEMMA_FOR.add("P");
        IGNORE_LEMMA_FOR.add("DET");
        IGNORE_LEMMA_FOR.add("CS");
        IGNORE_LEMMA_FOR.add("NPP");
        IGNORE_LEMMA_FOR.add("ET");
    }

    public static FrenchMorphologicalPropertiesSet getInstance() {
        synchronized (FrenchMorphologicalPropertiesSet.class) {
            if (instance == null) {
                long duration = System.nanoTime();
                instance = getInstance("/lefff-3.4.mlex");
                duration = System.nanoTime() - duration;
                LOGGER.info("Morphological dictionary loaded ({}ms)", duration / 1000000L);
            }
            return instance;
        }
    }

    static FrenchMorphologicalPropertiesSet getInstance(String resourcePath) {
        try (InputStream in = FrenchMorphologicalPropertiesSet.class.getResourceAsStream(resourcePath)) {
            return new FrenchMorphologicalPropertiesSet(in);
        } catch (IOException e) {
            throw new RuntimeIOException(e.getMessage(), e);
        }
    }

    FrenchMorphologicalPropertiesSet(InputStream in) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(in,"utf-8");
            BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line=reader.readLine())!=null) {
                String[] cols = line.split("\t");
                String type = cols[1];
                if (type.equals("advneg") || type.equals("clneg")) {
                    type = "adv";
                }
                HashMap<String, FrenchMorphologicalProperties> t = tagSet.computeIfAbsent(type, k -> new HashMap<>());
                t.put(cols[0],new FrenchMorphologicalProperties(cols[2],cols.length>3? cols[3] : null));
            }
        } catch (IOException e) {
            throw new RuntimeIOException(e.getMessage(), e);
        }
    }

    @Override
    public MorphologicalProperties getMorphologicalProperties(String lemma, String type, String word) {
        if (type.equals("NPP") || type.equals("PUNC")) {
            return new FrenchMorphologicalProperties(null,null);
        }
        else {
            List<String> correspondingType = CORRESPONDENCES.get(type);
            if (correspondingType == null) {
                return new FrenchMorphologicalProperties(null,null);
            } else {
                FrenchMorphologicalProperties result = null;
                for (int i=0; result==null && i<correspondingType.size(); i++) {
                    result = getMorphologicalPropertiesForType(correspondingType.get(i), word, type);
                }
                return result!=null? result : new FrenchMorphologicalProperties(null,null);
            }
        }
    }

    private FrenchMorphologicalProperties getMorphologicalPropertiesForType(String tt, String word, String type) {
        HashMap<String, FrenchMorphologicalProperties> set = tagSet.get(tt);
        if (set == null) {
            return null;
        } else {
            FrenchMorphologicalProperties result = set.get(word);
            if (result == null) {
                return null;
            } else {
                if (IGNORE_LEMMA_FOR.contains(type)) {
                    return new FrenchMorphologicalProperties(null, result.getPers(), result.getGender(), result.getNb(), result.getTps(), result.getMode());
                }
                return result;
            }
        }
    }
}
