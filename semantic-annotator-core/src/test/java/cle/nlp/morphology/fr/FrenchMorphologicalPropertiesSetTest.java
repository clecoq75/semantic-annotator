package cle.nlp.morphology.fr;

import cle.nlp.morphology.MorphologicalProperties;
import edu.stanford.nlp.io.RuntimeIOException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FrenchMorphologicalPropertiesSetTest {
    @BeforeClass
    public static void init() {
        FrenchMorphologicalPropertiesSet.getInstance();
    }

    @Test
    public void test_NPP() {
        FrenchMorphologicalPropertiesSet p = FrenchMorphologicalPropertiesSet.getInstance();
        MorphologicalProperties mp = p.getMorphologicalProperties("popo", "NPP", "popos");
        assertEquals(new MorphologicalProperties(null, null, null, null, null, null).toString(), mp.toString());
    }

    @Test
    public void test_PUNC() {
        FrenchMorphologicalPropertiesSet p = FrenchMorphologicalPropertiesSet.getInstance();
        MorphologicalProperties mp = p.getMorphologicalProperties("popo", "PUNC", "popos");
        assertEquals(new MorphologicalProperties(null, null, null, null, null, null).toString(), mp.toString());
    }

    @Test
    public void test_without_correspondence() {
        FrenchMorphologicalPropertiesSet p = FrenchMorphologicalPropertiesSet.getInstance();
        MorphologicalProperties mp = p.getMorphologicalProperties("popo", "ZIRKOL", "popos");
        assertEquals(new MorphologicalProperties(null, null, null, null, null, null).toString(), mp.toString());
    }

    @Test
    public void test_with_correspondence_but_no_match() {
        FrenchMorphologicalPropertiesSet p = FrenchMorphologicalPropertiesSet.getInstance();
        MorphologicalProperties mp = p.getMorphologicalProperties("popo", "CLS", "popos");
        assertEquals(new MorphologicalProperties(null, null, null, null, null, null).toString(), mp.toString());
    }

    @Test
    public void test_with_correspondence_and_match() {
        FrenchMorphologicalPropertiesSet p = FrenchMorphologicalPropertiesSet.getInstance();
        MorphologicalProperties mp = p.getMorphologicalProperties(null, "VPP", "démangiez");
        assertEquals(new MorphologicalProperties("démanger", "2", null, "p", "pres", "subj").toString(), mp.toString());
    }

    @Test
    public void test_with_correspondence_and_match_but_lemma_ignored() {
        FrenchMorphologicalPropertiesSet p = FrenchMorphologicalPropertiesSet.getInstance();
        MorphologicalProperties mp = p.getMorphologicalProperties(null, "CLR", "me");
        assertEquals(new MorphologicalProperties(null, "1", null, "s", null, null).toString(), mp.toString());
    }

    @Test(expected = RuntimeIOException.class)
    public void test_constructor_with_ioexception() {
        InputStream in  = mock(InputStream.class);
        new FrenchMorphologicalPropertiesSet(in);
    }
}
