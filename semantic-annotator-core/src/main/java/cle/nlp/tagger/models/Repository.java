package cle.nlp.tagger.models;

import cle.nlp.tagger.Error;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Repository {
    TaggerModel get(String name);
    Set<String> nameSet();
    Collection<TaggerModel> values();
    int size();
    boolean containsName(String name);
    Set<Map.Entry<String, TaggerModel>> entrySet();
    Collection<Error> getErrors();
}
