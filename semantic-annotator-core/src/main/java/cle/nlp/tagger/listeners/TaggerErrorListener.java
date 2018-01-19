package cle.nlp.tagger.listeners;

import cle.nlp.tagger.Rule;
import cle.nlp.tagger.Tagger;

public interface TaggerErrorListener {
    void onError(String s, Tagger tagger, Rule rule);
}
