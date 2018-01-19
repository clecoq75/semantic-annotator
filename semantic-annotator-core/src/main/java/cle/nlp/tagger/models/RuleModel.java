package cle.nlp.tagger.models;

import java.util.HashSet;
import java.util.Set;

public class RuleModel {
    private String pattern;
    private String regexp;
    private String substitutions;
    private Set<TagModel> tags = new HashSet<>();
    private Set<String> samples = new HashSet<>();
    private Set<String> applyIf = new HashSet<>();
    private Set<String> applyIfNot = new HashSet<>();

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getSubstitutions() {
        return substitutions;
    }

    public void setSubstitutions(String substitutions) {
        this.substitutions = substitutions;
    }

    public Set<TagModel> getTags() {
        return tags;
    }

    public void setTags(Set<TagModel> tags) {
        this.tags = tags!=null? tags : new HashSet<>();
    }

    public Set<String> getSamples() {
        return samples;
    }

    public void setSamples(Set<String> samples) {
        this.samples = samples!=null? samples : new HashSet<>();
    }

    public Set<String> getApplyIf() {
        return applyIf;
    }

    public void setApplyIf(Set<String> applyIf) {
        this.applyIf = applyIf!=null? applyIf : new HashSet<>();
    }

    public Set<String> getApplyIfNot() {
        return applyIfNot;
    }

    public void setApplyIfNot(Set<String> applyIfNot) {
        this.applyIfNot = applyIfNot!=null? applyIfNot : new HashSet<>();
    }

    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }
}
