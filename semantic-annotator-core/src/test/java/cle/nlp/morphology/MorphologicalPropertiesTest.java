package cle.nlp.morphology;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MorphologicalPropertiesTest {
    @Test
    public void test_toString() {
        MorphologicalProperties m = new MorphologicalProperties("test", "2", null, "p", null, null);
        assertTrue(m.toString().contains("lemma=test"));
        assertTrue(m.toString().contains("pers=2"));
        assertTrue(m.toString().contains("nb=p"));
        assertEquals(22, m.toString().length());
    }

    @Test
    public void test_getters() {
        MorphologicalProperties m = new MorphologicalProperties("test", "2", "f", "p", "fut", "ind");
        assertEquals("test", m.getLemma());
        assertEquals("2", m.getPers());
        assertEquals("f", m.getGender());
        assertEquals("p", m.getNb());
        assertEquals("fut", m.getTps());
        assertEquals("ind", m.getMode());
    }

    @Test
    public void test_setters() {
        MorphologicalProperties m = new MorphologicalProperties(null, null, null, null, null, null);
        m.setLemmaValue("test");
        assertEquals("test", m.getLemma());
        m.setPersValue("2");
        assertEquals("2", m.getPers());
        m.setGenderValue("f");
        assertEquals("f", m.getGender());
        m.setNbValue("p");
        assertEquals("p", m.getNb());
        m.setTpsValue("fut");
        assertEquals("fut", m.getTps());
        m.setModeValue("ind");
        assertEquals("ind", m.getMode());
    }

    @Test
    public void test_getters_with_null() {
        MorphologicalProperties m = new MorphologicalProperties(null, null, null, null, null, null);
        assertNull(m.getLemma());
        assertNull(m.getPers());
        assertNull(m.getGender());
        assertNull(m.getNb());
        assertNull(m.getTps());
        assertNull(m.getMode());
    }

    @Test
    public void test_copy() {
        MorphologicalProperties m = new MorphologicalProperties("test", "2", "f", "p", "fut", "ind");
        m = m.copy();
        assertEquals("test", m.getLemma());
        assertEquals("2", m.getPers());
        assertEquals("f", m.getGender());
        assertEquals("p", m.getNb());
        assertEquals("fut", m.getTps());
        assertEquals("ind", m.getMode());
    }
}
