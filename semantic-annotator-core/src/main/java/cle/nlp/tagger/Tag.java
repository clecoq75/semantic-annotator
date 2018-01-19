package cle.nlp.tagger;

import java.util.HashSet;
import java.util.Set;

public class Tag {
    private String value;
    private Rule rule;
    private Tagger tagger;
    private String text;

    public Tag(String value) {
        this(value, null, null, null);
    }

    public Tag(String value, Rule rule, Tagger tagger, String text) {
        if (value==null) {
            throw new IllegalArgumentException("Tag value can't be null");
        }
        this.value = value;
        this.rule = rule;
        this.tagger = tagger;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public Rule getRule() {
        return rule;
    }

    public Tagger getTagger() {
        return tagger;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return value + " (text='" + text + '\'' +
                ", tagger=" + (tagger!=null? tagger.getName() : "null") +
                ", rule=" + (rule!=null? rule.toString() : "null") +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        return this == o
                || o != null
                    && getClass() == o.getClass()
                    && value.equals(((Tag) o).getValue());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static Set<Tag> toTagSet(Set<String> stringSet) {
        Set<Tag> result = new HashSet<>();
        if (stringSet!=null && !stringSet.isEmpty()) {
            for (String s : stringSet) {
                Tag t = new Tag(s);
                result.add(t);
            }
        }
        return result;
    }
}
