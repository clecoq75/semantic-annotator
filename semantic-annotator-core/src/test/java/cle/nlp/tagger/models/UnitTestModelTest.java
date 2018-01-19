package cle.nlp.tagger.models;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class UnitTestModelTest {
    @Test
    public void test_setVerbatim_with_null() {
        UnitTestModel u = new UnitTestModel();
        u.setVerbatim(null);
        assertEquals(new HashSet<>(), u.getVerbatim());
    }

    @Test
    public void test_setVerbatim() {
        UnitTestModel u = new UnitTestModel();
        Set<String> v = new HashSet<>();
        v.add("lopo");
        u.setVerbatim(v);
        assertEquals(v, u.getVerbatim());
    }

    @Test
    public void test_setTags_with_null() {
        UnitTestModel u = new UnitTestModel();
        u.setTags(null);
        assertEquals(new HashSet<>(), u.getTags());
    }

    @Test
    public void test_setTags() {
        UnitTestModel u = new UnitTestModel();
        Set<String> v = new HashSet<>();
        v.add("lopo");
        u.setTags(v);
        assertEquals(v, u.getTags());
    }
}
