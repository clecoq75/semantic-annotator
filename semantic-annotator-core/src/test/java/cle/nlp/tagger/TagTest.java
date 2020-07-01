package cle.nlp.tagger;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class TagTest {
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_with_null_label() {
        new Tag(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_with_null_label2() {
        new Tag(null, null,null,null);
    }

    @Test
    public void test_equals() {
        Tag tag = new Tag("pop");
        Tag tag2 = new Tag("pop", null, null, "titi");
        Tag tag3 = new Tag("popy", null, null, "titi2");
        assertEquals(tag, tag);
        assertNotEquals(tag, tag3);
        assertNotNull(tag);
        assertNotEquals(tag, new ArrayList<>());
        assertEquals(tag.hashCode(), tag.hashCode());
        assertEquals(tag, tag2);
        assertEquals(tag.hashCode(), tag2.hashCode());
    }

    @Test
    public void test_constructor() {
        String value = "v";
        Rule r = mock(Rule.class);
        Tagger t = mock(Tagger.class);
        String text = "t";
        Tag tag = new Tag(value, r,t,text);
        assertEquals(value, tag.getValue());
        assertEquals(r, tag.getRule());
        assertEquals(t, tag.getTagger());
        assertEquals(text, tag.getText());
    }

    @Test
    public void test_toTagSet_with_null() {
        assertEquals(new HashSet<>(), Tag.toTagSet(null));
    }

    @Test
    public void test_toTagSet_with_empty() {
        assertEquals(new HashSet<>(), Tag.toTagSet(new HashSet<>()));
    }

    @Test
    public void test_toTagSet() {
        Set<String> input = new HashSet<>();
        input.add("tyty");
        Set<Tag> tags = Tag.toTagSet(input);
        assertEquals(1, tags.size());
        assertTrue(tags.contains(new Tag("tyty")));
    }
}
