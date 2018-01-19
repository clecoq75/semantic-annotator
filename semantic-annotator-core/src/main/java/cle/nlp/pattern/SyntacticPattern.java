package cle.nlp.pattern;

import cle.nlp.partofspeech.Node;

public interface SyntacticPattern {
    SyntacticMatcher matcher(Node node);
    SyntacticMatcher matcher(Node node, int fromIndex);
    SyntacticPattern getParent();
}
