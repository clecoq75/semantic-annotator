package cle.nlp.tagger.models;

import java.util.HashSet;
import java.util.Set;

public class UnitTestModel {
    private Set<String> verbatim = new HashSet<>();
    private Set<String> tags;

    public Set<String> getVerbatim() {
        return verbatim;
    }

    public void setVerbatim(Set<String> verbatim) {
        this.verbatim = verbatim!=null? verbatim : new HashSet<>();
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags!=null? tags : new HashSet<>();
    }
}
