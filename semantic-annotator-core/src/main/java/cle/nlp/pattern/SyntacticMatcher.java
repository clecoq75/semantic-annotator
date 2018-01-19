package cle.nlp.pattern;

import cle.nlp.partofspeech.Node;

import java.util.List;

public interface SyntacticMatcher {
    boolean matches();
    List<Node> getMatchedNodes();
    int getMatchedNodesCount();
    List<SyntacticMatcher> getSubMatcherList();
    int getRelevance();
    SyntacticPattern getSourcePattern();
}
