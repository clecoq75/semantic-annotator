package cle.nlp.tagger.models;

import java.util.ArrayList;
import java.util.List;

public class TaggerModel {
    private List<String> importRules = new ArrayList<>();
    private List<String> collection = new ArrayList<>();
    private List<RuleModel> rules = new ArrayList<>();
    private List<UnitTestModel> unitTests = new ArrayList<>();

    public List<RuleModel> getRules() {
        return rules;
    }

    public void setRules(List<RuleModel> rules) {
        this.rules = rules!=null? rules : new ArrayList<>();
    }

    public List<UnitTestModel> getUnitTests() {
        return unitTests;
    }

    public void setUnitTests(List<UnitTestModel> unitTests) {
        this.unitTests = unitTests!=null? unitTests : new ArrayList<>();
    }

    public List<String> getImportRules() {
        return importRules;
    }

    public void setImportRules(List<String> importRules) {
        this.importRules = importRules!=null? importRules : new ArrayList<>();
    }

    public List<String> getCollection() {
        return collection;
    }

    public void setCollection(List<String> taggerList) {
        this.collection = taggerList!=null? taggerList : new ArrayList<>();
    }
}
