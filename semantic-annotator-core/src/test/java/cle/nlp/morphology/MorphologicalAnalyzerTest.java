package cle.nlp.morphology;

import cle.nlp.SupportedLanguages;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MorphologicalAnalyzerTest {
    @Test
    public void test_get_french() {
        MorphologicalAnalyzer fr = MorphologicalAnalyzer.getInstance(SupportedLanguages.FR);
        MorphologicalProperties m = fr.getMorphologicalProperties(null, "V", "aurait");
        assertEquals("cond", m.getMode());
        assertEquals("s", m.getNb());
        assertEquals("pres", m.getTps());
        assertEquals("avoir", m.getLemma());
    }

    @Test
    public void test_get_english() {
        MorphologicalAnalyzer fr = MorphologicalAnalyzer.getInstance(SupportedLanguages.EN);
        MorphologicalProperties m = fr.getMorphologicalProperties("have", "V", "have");
        assertEquals("have", m.getLemma());
    }
}
