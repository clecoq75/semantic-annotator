package cle.nlp.pattern;

import cle.nlp.partofspeech.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SentenceSyntacticMatcher implements SyntacticMatcher {
    private static final List<Node> EMPTY = Collections.unmodifiableList(new ArrayList<>());

    public static final SentenceSyntacticMatcher NO_MATCH = new SentenceSyntacticMatcher(false, null, null);

    private List<SyntacticMatcher> subMatchers;
    private boolean matches;
    private int relevance = 0;
    private SyntacticPattern sourcePattern;

    SentenceSyntacticMatcher(boolean matches, List<SyntacticMatcher> subMatchers, SyntacticPattern sourcePattern) {
        this.matches = matches;
        this.subMatchers = subMatchers;
        this.sourcePattern = sourcePattern;
        if (matches) {
            for (SyntacticMatcher sm : subMatchers) {
                relevance += sm.getRelevance();
            }
        }
    }

    @Override
    public boolean matches() {
        return matches;
    }

    @Override
    public List<Node> getMatchedNodes() {
        return EMPTY;
    }

    @Override
    public int getMatchedNodesCount() {
        int count = 0;
        for (SyntacticMatcher sm : subMatchers) {
            count += sm.getMatchedNodesCount();
        }
        return count;
    }

    @Override
    public int getRelevance() {
        return relevance;
    }

    @Override
    public SyntacticPattern getSourcePattern() {
        return sourcePattern;
    }

    public List<SyntacticMatcher> getSubMatcherList() {
        return subMatchers;
    }

    @Override
    public String toString() {
        if (!matches) {
            return "not matching";
        }
        else {
            StringBuilder sb = new StringBuilder();
            for (SyntacticMatcher sm : subMatchers) {
                if (sb.length()>0) {
                    sb.append(' ');
                }
                sb.append('(').append(sm.toString()).append(')');
            }
            return sb.toString();
        }
    }
}
