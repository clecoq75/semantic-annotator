package cle.nlp.tagger;

import cle.nlp.tagger.models.UnitTestModel;

import java.util.HashSet;
import java.util.Set;

public class UnitTest {
    private final Set<String> verbatim;
    private final Set<Tag> tags = new HashSet<>();

    public UnitTest(UnitTestModel model) {
        verbatim = model.getVerbatim();
        if (!model.getTags().isEmpty()) {
            model.getTags().forEach(s -> tags.add(new Tag(s, null, null, null)));
        }
    }

    public Set<String> getVerbatim() {
        return verbatim;
    }

    public Set<Tag> getTags() {
        return tags;
    }
}
