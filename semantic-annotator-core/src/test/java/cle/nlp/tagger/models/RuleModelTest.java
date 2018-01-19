package cle.nlp.tagger.models;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class RuleModelTest {
    @Test
    public void test_setPattern() {
        RuleModel r = new RuleModel();
        r.setPattern("toto");
        assertEquals("toto", r.getPattern());
    }

    @Test
    public void test_setSubstitutions() {
        RuleModel r = new RuleModel();
        r.setSubstitutions("toto");
        assertEquals("toto", r.getSubstitutions());
    }

    @Test
    public void test_setRegexp() {
        RuleModel r = new RuleModel();
        r.setRegexp("toto");
        assertEquals("toto", r.getRegexp());
    }

    @Test
    public void test_setTags_with_null() {
        RuleModel r = new RuleModel();
        r.setTags(null);
        assertEquals(new HashSet<>(), r.getTags());
    }

    @Test
    public void test_setTags() {
        RuleModel r = new RuleModel();
        Set<TagModel> s = new HashSet<>();
        s.add(new TagModel());
        r.setTags(s);
        assertEquals(s, r.getTags());
    }

    @Test
    public void test_setSamples_with_null() {
        RuleModel r = new RuleModel();
        r.setSamples(null);
        assertEquals(new HashSet<>(), r.getSamples());
    }

    @Test
    public void test_setSamples() {
        RuleModel r = new RuleModel();
        Set<String> s = new HashSet<>();
        s.add("yoop");
        r.setSamples(s);
        assertEquals(s, r.getSamples());
    }

    @Test
    public void test_setApplyIf() {
        RuleModel r = new RuleModel();
        Set<String> s = new HashSet<>();
        s.add("riri");
        r.setApplyIf(s);
        assertEquals(s, r.getApplyIf());
    }

    @Test
    public void test_setApplyIf_with_null() {
        RuleModel r = new RuleModel();
        r.setApplyIf(null);
        assertEquals(new HashSet<>(), r.getApplyIf());
    }

    @Test
    public void test_setApplyIfNot() {
        RuleModel r = new RuleModel();
        Set<String> s = new HashSet<>();
        s.add("riri");
        r.setApplyIfNot(s);
        assertEquals(s, r.getApplyIfNot());
    }

    @Test
    public void test_setApplyIfNot_with_null() {
        RuleModel r = new RuleModel();
        r.setApplyIfNot(null);
        assertEquals(new HashSet<>(), r.getApplyIfNot());
    }
}
