package cle.nlp.pattern;

import cle.nlp.morphology.MorphologicalProperties;
import cle.nlp.partofspeech.Node;
import cle.nlp.pattern.exceptions.InvalidPatternException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TokenSyntacticMatcherTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws InvalidPatternException {
        Node n1 = new Node("pok", "KI", null);
        Node n2 = new Node("pok", "KI", new MorphologicalProperties("lemma", "1", "g", "p", null, null));
        SyntacticPattern p = new TokenSyntacticPattern("pok:KI", null);
        return Arrays.asList(new Object[][] {
                {  new TokenSyntacticMatcher(true, n1, 6, p), true, Collections.singletonList(n1), 1, 6, p, "pok:KI" },
                {  new TokenSyntacticMatcher(true, n2, 3, p), true, Collections.singletonList(n2), 1, 3, p, "pok:KI;gender=g;lemma=lemma;nb=p;pers=1" },
                {  new TokenSyntacticMatcher(false, null, 0, null), false, new ArrayList<>(), 0, 0, null, "not matching" }
        });
    }

    private TokenSyntacticMatcher matcher;
    private boolean matches;
    private Collection<Node> matchedNodes;
    private int matchedNodesCount;
    private int relevance;
    private SyntacticPattern source;
    private String toStringResult;

    public TokenSyntacticMatcherTest(TokenSyntacticMatcher matcher, boolean matches, Collection<Node> matchedNodes,
                                     int matchedNodesCount, int relevance, SyntacticPattern source, String toStringResult) {
        this.matcher = matcher;
        this.matches = matches;
        this.matchedNodes = matchedNodes;
        this.matchedNodesCount = matchedNodesCount;
        this.relevance = relevance;
        this.source = source;
        this.toStringResult = toStringResult;
    }

    @Test
    public void test() {
        assertEquals(matches, matcher.matches());
        assertEquals(matchedNodes, matcher.getMatchedNodes());
        assertEquals(matchedNodesCount, matcher.getMatchedNodesCount());
        assertEquals(new ArrayList<>(), matcher.getSubMatcherList());
        assertEquals(relevance, matcher.getRelevance());
        assertEquals(source, matcher.getSourcePattern());
        assertEquals(toStringResult, matcher.toString());
    }
}
