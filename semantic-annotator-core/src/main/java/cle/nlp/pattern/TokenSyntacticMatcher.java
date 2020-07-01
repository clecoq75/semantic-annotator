package cle.nlp.pattern;

import cle.nlp.partofspeech.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TokenSyntacticMatcher implements SyntacticMatcher {
    private static final List<Node> EMPTY_NODE_LIST = Collections.unmodifiableList(new ArrayList<>());
    private static final List<SyntacticMatcher> EMPTY_MATCHER_LIST = Collections.unmodifiableList(new ArrayList<>());
    public static final TokenSyntacticMatcher NO_MATCH = new TokenSyntacticMatcher(false, null, 0, null);

    private List<Node> source;
    private final boolean matches;
    private int relevance;
    private SyntacticPattern sourcePattern;

    TokenSyntacticMatcher(boolean matches, Node node, int relevance, SyntacticPattern sourcePattern) {
        this.matches = matches;
        if (!matches) {
            if (node!=null) {
                throw new IllegalArgumentException("Node can't be specified when not matching");
            }
            else if (relevance!=0) {
                throw new IllegalArgumentException("Invalid relevance for non matching : "+relevance);
            }
            else if (sourcePattern!=null) {
                throw new IllegalArgumentException("Source pattern can't be specified when not matching");
            }
        }
        else {
            if (node==null) {
                throw new IllegalArgumentException("Node must be specified when matching");
            }
            else if (relevance<1) {
                throw new IllegalArgumentException("Invalid relevance for matching : "+relevance);
            }
            else if (sourcePattern==null) {
                throw new IllegalArgumentException("Source pattern must be specified when matching");
            }
        }
        this.relevance = relevance;
        this.sourcePattern = sourcePattern;
        source = node!=null? Collections.singletonList(node) : EMPTY_NODE_LIST;
    }

    @Override
    public boolean matches() {
        return matches;
    }

    @Override
    public List<Node> getMatchedNodes() {
        return source;
    }

    @Override
    public int getMatchedNodesCount() {
        return matches? 1 : 0;
    }

    @Override
    public List<SyntacticMatcher> getSubMatcherList() {
        return EMPTY_MATCHER_LIST;
    }

    @Override
    public int getRelevance() {
        return relevance;
    }

    @Override
    public SyntacticPattern getSourcePattern() {
        return sourcePattern;
    }

    @Override
    public String toString() {
        if (!matches) {
            return "not matching";
        }
        else {
            return source.get(0).toString();
        }
    }
}
