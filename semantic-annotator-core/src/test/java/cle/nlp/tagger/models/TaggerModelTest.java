package cle.nlp.tagger.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TaggerModelTest {
    @Test
    public void test_setRules() {
        TaggerModel tm = new TaggerModel();
        List<RuleModel> list = new ArrayList<>();
        list.add(new RuleModel());
        tm.setRules(list);
        assertEquals(list, tm.getRules());
    }

    @Test
    public void test_setRules_with_null() {
        TaggerModel tm = new TaggerModel();
        tm.setRules(null);
        assertNotNull(tm.getRules());
        assertEquals(0, tm.getRules().size());
    }

    @Test
    public void test_UnitTestModel() {
        TaggerModel tm = new TaggerModel();
        List<UnitTestModel> list = new ArrayList<>();
        list.add(new UnitTestModel());
        tm.setUnitTests(list);
        assertEquals(list, tm.getUnitTests());
    }

    @Test
    public void test_UnitTestModel_with_null() {
        TaggerModel tm = new TaggerModel();
        tm.setUnitTests(null);
        assertNotNull(tm.getUnitTests());
        assertEquals(0, tm.getUnitTests().size());
    }

    @Test
    public void test_setImportRules() {
        TaggerModel tm = new TaggerModel();
        List<String> list = new ArrayList<>();
        list.add("toto");
        tm.setImportRules(list);
        assertEquals(list, tm.getImportRules());
    }

    @Test
    public void test_setImportRules_with_null() {
        TaggerModel tm = new TaggerModel();
        tm.setImportRules(null);
        assertNotNull(tm.getImportRules());
        assertEquals(0, tm.getImportRules().size());
    }

    @Test
    public void test_setCollections() {
        TaggerModel tm = new TaggerModel();
        List<String> list = new ArrayList<>();
        list.add("toto");
        tm.setCollection(list);
        assertEquals(list, tm.getCollection());
    }

    @Test
    public void test_setCollection_with_null() {
        TaggerModel tm = new TaggerModel();
        tm.setCollection(null);
        assertNotNull(tm.getCollection());
        assertEquals(0, tm.getCollection().size());
    }
}
