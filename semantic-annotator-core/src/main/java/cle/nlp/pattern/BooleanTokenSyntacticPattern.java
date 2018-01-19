package cle.nlp.pattern;

import cle.nlp.partofspeech.Node;
import cle.nlp.pattern.exceptions.InvalidPatternException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static cle.nlp.pattern.TokenSyntacticMatcher.NO_MATCH;

public class BooleanTokenSyntacticPattern implements SyntacticPattern {
    private List<TokenSyntacticPattern> matchers = new ArrayList<>();
    private SyntacticPattern parent;

    public BooleanTokenSyntacticPattern(String expression, SyntacticPattern parent) throws InvalidPatternException {
        this.parent = parent;
        String[] expr = expression.split("\\|");
        for (String e : expr) {
            if (!StringUtils.isBlank(e)) {
                matchers.add(new TokenSyntacticPattern(e.trim(), this));
            }
        }
    }

    @Override
    public SyntacticMatcher matcher(Node node, int fromIndex) {
        return matcher(node);
    }

    @Override
    public SyntacticMatcher matcher(Node node) {
        int bestRelevance = 0;
        boolean matches = false;
        for (TokenSyntacticPattern p : matchers) {
            SyntacticMatcher m = p.matcher(node);
            if (m.matches()) {
                matches = true;
                if (m.getRelevance()>bestRelevance) {
                    bestRelevance = m.getRelevance();
                }
            }
        }

        if (!matches) {
            return NO_MATCH;
        }
        else {
            return new TokenSyntacticMatcher(true, node, bestRelevance, this);
        }
    }

    @Override
    public SyntacticPattern getParent() {
        return parent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (TokenSyntacticPattern t : matchers) {
            if (sb.length()>0) {
                sb.append(" | ");
            }
            sb.append(t.toString());
        }
        return sb.toString();
    }
}
