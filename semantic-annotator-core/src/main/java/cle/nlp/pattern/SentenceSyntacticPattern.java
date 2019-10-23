package cle.nlp.pattern;

import cle.nlp.partofspeech.Node;
import cle.nlp.pattern.exceptions.InvalidPatternException;
import cle.nlp.pattern.exceptions.PatternException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static cle.nlp.pattern.MatchQuantifier.*;
import static cle.nlp.pattern.SentenceSyntacticMatcher.NO_MATCH;
import static cle.nlp.pattern.TokenSyntacticPattern.REGEX_FULL;

public class SentenceSyntacticPattern implements SyntacticPattern {
    private static final Logger LOGGER = LoggerFactory.getLogger(SentenceSyntacticPattern.class);

    private static class AnalysisState {
        int patternIndex;
        int nodeIndex;
        List<SyntacticMatcher> matcherList = new ArrayList<>();
        List<Node> nodeList;

        public AnalysisState(int patternIndex, int nodeIndex, List<Node> nodeList) {
            this.patternIndex = patternIndex;
            this.nodeIndex = nodeIndex;
            this.nodeList = nodeList;
        }
    }

    public static final String REGEX = "^([()| ]*"+REGEX_FULL+"[()| ?*+]*)*$";
    public static final Pattern PATTERN = Pattern.compile(REGEX);

    private List<SyntacticPattern> list = new ArrayList<>();
    private MatchQuantifier quantifier;
    private List<SentenceSyntacticPattern> groups;
    private SyntacticPattern parent;

    public static SentenceSyntacticPattern getInstance(String expression) throws InvalidPatternException {
        return getInstance(expression, false);
    }

    public static SentenceSyntacticPattern getInstance(String expression, boolean optimize) throws InvalidPatternException {
        if (!isValidExpression(expression)) {
            throw new InvalidPatternException("Invalid sentence expression : "+expression);
        }
        SentenceSyntacticPattern result = new SentenceSyntacticPattern(expression, optimize);
        result.collectGroups();
        return result;
    }

    private SentenceSyntacticPattern(String expression, boolean optimize) throws InvalidPatternException {
        this(expression, ONCE, null);
        if (optimize) {
            optimizePattern(true, expression);
            optimizePattern(false, expression);
        }
    }

    private void optimizePattern(boolean firstElement, String expression) {
        boolean warnLogged = false;
        while ((firstElement && firstIsUseless(list)) || (!firstElement && lastIsUseless(list))) {
            SyntacticPattern last = list.get(firstElement? 0 : list.size()-1);
            if (!warnLogged) {
                LOGGER.warn("Useless expression '{}' at the {} of pattern '{}'", last, firstElement? "start" : "end", expression);
                warnLogged = true;
            }
            list.remove(firstElement? 0 : list.size()-1);
        }
    }

    private SentenceSyntacticPattern(String expression, MatchQuantifier quantifier, SyntacticPattern parent) throws InvalidPatternException {
        this.quantifier = quantifier;
        this.parent = parent;
        StringBuilder current = new StringBuilder();
        int charIndex=0;
        while (charIndex<expression.length()) {
            char c = expression.charAt(charIndex);
            if (c==' ') {
                if (current.length()>0) {
                    list.add(new BooleanTokenSyntacticPattern(current.toString(), this));
                    current = new StringBuilder();
                }
            }
            else if (c=='(') {
                charIndex = buildGroup(expression, charIndex);
            }
            else {
                current.append(c);
            }

            charIndex++;
        }
        if (current.length()>0) {
            list.add(new BooleanTokenSyntacticPattern(current.toString(), this));
        }
    }

    private int buildGroup(String expression, int charIndex) throws InvalidPatternException {
        int closingParenthesis = findClosingParenthesis(expression,charIndex);
        String subListContent = expression.substring(charIndex+1, closingParenthesis);
        charIndex = closingParenthesis+1;
        MatchQuantifier subListQuantifier = ONCE;
        if (closingParenthesis+1<expression.length()) {
            char q = expression.charAt(closingParenthesis+1);
            if ("?*+".indexOf(q)>-1) {
                subListQuantifier = fromChar(q);
                charIndex++;
            }
        }
        list.add(new SentenceSyntacticPattern(subListContent,subListQuantifier,this));
        return charIndex;
    }

    private boolean lastIsUseless(List<SyntacticPattern> syntacticPatterns) {
        return isUseless(syntacticPatterns, false);
    }

    private boolean firstIsUseless(List<SyntacticPattern> syntacticPatterns) {
        return isUseless(syntacticPatterns, true);
    }

    private boolean isUseless(List<SyntacticPattern> syntacticPatterns, boolean firstElement) {
        if (syntacticPatterns.size()>1) {
            SyntacticPattern element = firstElement? syntacticPatterns.get(0) : syntacticPatterns.get(syntacticPatterns.size()-1);
            return isUseless(element);
        }
        return false;
    }

    private boolean isUseless(SyntacticPattern element) {
        return element.getClass().equals(SentenceSyntacticPattern.class)
                && (((SentenceSyntacticPattern) element).quantifier == NONE_OR_ONCE || ((SentenceSyntacticPattern) element).quantifier == ANY);
    }

    public void collectGroups() {
        groups = new ArrayList<>();
        collectGroups(this, groups);
    }

    private static void collectGroups(SentenceSyntacticPattern sp, List<SentenceSyntacticPattern> groups) {
        groups.add(sp);
        for (SyntacticPattern c : sp.list) {
            if (c.getClass().equals(SentenceSyntacticPattern.class)) {
                SentenceSyntacticPattern c1 = (SentenceSyntacticPattern)c;
                collectGroups(c1, groups);
            }
        }
    }

    static int findClosingParenthesis(String text, int openingParenthesis) {
        if (text==null) {
            throw new IllegalArgumentException("Text can not be null");
        }
        else if (openingParenthesis<0 || openingParenthesis>=text.length()) {
            throw new IllegalArgumentException("Opening parenthesis index is not valid : "+openingParenthesis);
        }
        else if (text.charAt(openingParenthesis)!='(') {
            throw new IllegalArgumentException("Opening parenthesis index is not valid (not a parenthesis) : "+openingParenthesis);
        }
        else {
            int opened = 1;
            for (int i = openingParenthesis + 1; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c == '(') {
                    opened++;
                } else if (c == ')') {
                    opened--;
                    if (opened == 0) {
                        return i;
                    }
                }
            }
            return -1;
        }
    }

    public static boolean isValidExpression(String expression) {
        return PATTERN.matcher(expression).matches() && validateParenthesis(expression);
    }

    static boolean validateParenthesis(String expression) {
        int opened = 0;
        char previous = '#';
        for (int i=0; i<expression.length(); i++) {
            char c = expression.charAt(i);
            switch (c) {
                case '(':
                    opened++;
                    break;
                case ')':
                    opened--;
                    if (opened < 0) {
                        return false;
                    }
                    break;
                case '*':
                case '+':
                    if (previous != ')') {
                        return false;
                    }
                    break;
                default :
            }
            previous = c;
        }
        return opened==0;
    }

    @Override
    public SyntacticMatcher matcher(Node node) {
        return matcher(node, 0);
    }

    @Override
    public SyntacticMatcher matcher(Node node, int fromIndex) {
        List<Node> nodeList = node.getChildren();
        for (int i=fromIndex; i<nodeList.size(); i++) {
            SentenceSyntacticMatcher m = listMatcher(nodeList, i, false);
            if (m.matches()) {
                return m;
            }
        }
        return NO_MATCH;
    }

    @Override
    public SyntacticPattern getParent() {
        return parent;
    }

    public SentenceSyntacticMatcher listMatcher(List<Node> nodeList) {
        return  listMatcher(nodeList, 0, true);
    }

    public SentenceSyntacticMatcher listMatcher(List<Node> nodeListP, int fromIndex, boolean untilTheEndOfNodeList) {
        AnalysisState analysisState = new AnalysisState(0, fromIndex, nodeListP);

        while (analysisState.patternIndex<list.size()) {
            if (!verifyCurrentPattern(analysisState)) {
                return NO_MATCH;
            }

            analysisState.patternIndex++;
        }

        if (analysisState.nodeIndex!= analysisState.nodeList.size() && untilTheEndOfNodeList) {
            return NO_MATCH;
        }
        else {
            return new SentenceSyntacticMatcher(true, analysisState.matcherList, this);
        }
    }

    private boolean verifyCurrentPattern(AnalysisState analysisState) {
        if (analysisState.nodeIndex >= analysisState.nodeList.size()) {
            return verifyEndingPatterns(analysisState);
        }
        else {
            SyntacticPattern p = list.get(analysisState.patternIndex);
            if (p.getClass().equals(BooleanTokenSyntacticPattern.class)) {
                return verifySimplePattern(analysisState, p);
            } else {
                return verifyListPattern(analysisState, (SentenceSyntacticPattern) p);
            }
        }

    }

    private boolean verifyEndingPatterns(AnalysisState analysisState) {
        while (analysisState.patternIndex<list.size()) {
            SyntacticPattern p = list.get(analysisState.patternIndex++);
            if (p.getClass().equals(BooleanTokenSyntacticPattern.class)) {
                return false;
            }
            else {
                SentenceSyntacticPattern pattern = (SentenceSyntacticPattern)p;
                if (pattern.quantifier==ONCE || pattern.quantifier==AT_LEAST_ONCE) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean verifyListPattern(AnalysisState analysisState, SentenceSyntacticPattern pattern) {
        switch (pattern.quantifier) {
            case NONE_OR_ONCE:
                return verifyListPatternNoneOrOnce(analysisState, pattern);
            case ANY:
                return verifyListPatternAny(analysisState, pattern);
            default:
                return verifyListPatternOnceOrAtLeastOnce(analysisState, pattern);
        }
    }

    private boolean verifyListPatternNoneOrOnce(AnalysisState analysisState, SentenceSyntacticPattern pattern) {
        SentenceSyntacticMatcher m = pattern.listMatcher(analysisState.nodeList, analysisState.nodeIndex, false);
        if (m.matches()) {
            analysisState.nodeIndex = validateIfNextPatternDoesNotMatchBetter(m, analysisState.patternIndex,
                    analysisState.nodeList, analysisState.nodeIndex, analysisState.matcherList);
        }
        return true;
    }

    private boolean verifyListPatternAny(AnalysisState analysisState, SentenceSyntacticPattern pattern) {
        SentenceSyntacticMatcher m;
        int initialNodeIdx;
        do {
            initialNodeIdx = analysisState.nodeIndex;
            m = pattern.listMatcher(analysisState.nodeList, analysisState.nodeIndex, false);
            if (m.matches()) {
                analysisState.nodeIndex = validateIfNextPatternDoesNotMatchBetter(m, analysisState.patternIndex,
                        analysisState.nodeList, analysisState.nodeIndex, analysisState.matcherList);
            }
        } while (m.matches()
                && initialNodeIdx!=analysisState.nodeIndex
                && analysisState.nodeIndex< analysisState.nodeList.size());

        return true;
    }

    private boolean verifyListPatternOnceOrAtLeastOnce(AnalysisState analysisState, SentenceSyntacticPattern pattern) {
        SentenceSyntacticMatcher m = pattern.listMatcher(analysisState.nodeList, analysisState.nodeIndex, false);
        if (m.matches()) {
            analysisState.matcherList.add(m);
            analysisState.nodeIndex += m.getMatchedNodesCount();
        } else {
            return false;
        }

        if (pattern.quantifier==AT_LEAST_ONCE) {
            int initialNodeIdx;
            do {
                initialNodeIdx = analysisState.nodeIndex;
                m = pattern.listMatcher(analysisState.nodeList, analysisState.nodeIndex, false);
                if (m.matches()) {
                    analysisState.nodeIndex = validateIfNextPatternDoesNotMatchBetter(m, analysisState.patternIndex,
                            analysisState.nodeList, analysisState.nodeIndex, analysisState.matcherList);
                }
            } while (m.matches()
                    && initialNodeIdx != analysisState.nodeIndex
                    && analysisState.nodeIndex < analysisState.nodeList.size());
        }

        return true;
    }

    private boolean verifySimplePattern(AnalysisState analysisState, SyntacticPattern p) {
        Node n = analysisState.nodeList.get(analysisState.nodeIndex++);
        SyntacticMatcher m = p.matcher(n);
        if (m.matches()) {
            analysisState.matcherList.add(m);
        } else {
            return false;
        }

        return true;
    }

    private int validateIfNextPatternDoesNotMatchBetter(SyntacticMatcher m, int patternIdx, List<Node> nodeList, int nodeIdx, List<SyntacticMatcher> matchers) {
        SyntacticMatcher nextMatcher = getMatcherOnNextPattern(patternIdx, nodeList, nodeIdx);
        if (nextMatcher==null || !nextMatcher.matches() || nextMatcher.getRelevance()<=m.getRelevance()) {
            matchers.add(m);
            nodeIdx += m.getMatchedNodesCount();
        }
        return nodeIdx;
    }

    private SyntacticMatcher getMatcherOnNextPattern(int patternIdx, List<Node> nodeList, int nodeIdx) {
        SyntacticPattern nextPattern = patternIdx+1<list.size()? list.get(patternIdx+1) : null;
        SyntacticMatcher nextMatcher = null;
        if (nextPattern!=null) {
            if (nextPattern.getClass().equals(BooleanTokenSyntacticPattern.class)) {
                nextMatcher = nextPattern.matcher(nodeList.get(nodeIdx));
            }
            else {
                nextMatcher = ((SentenceSyntacticPattern)nextPattern).listMatcher(nodeList,nodeIdx, false);
            }
        }
        return nextMatcher;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");

        for (SyntacticPattern sm : list) {
            if (sb.length()>1) {
                sb.append(' ');
            }
            sb.append(sm.toString());
        }

        sb.append(")");
        if (quantifier==ANY) {
            sb.append("*");
        }
        else if (quantifier==AT_LEAST_ONCE) {
            sb.append("+");
        }
        else if (quantifier==NONE_OR_ONCE) {
            sb.append("?");
        }
        return sb.toString();
    }

    public SentenceSyntacticPattern getGroup(int groupIndex) throws PatternException {
        if (groupIndex<1 || groupIndex>=groups.size()) {
            throw new PatternException("Invalid group index : "+groupIndex+" for pattern '"+toString()+"'");
        }
        else {
            return groups.get(groupIndex);
        }
    }

    public boolean matchesEverything() {
        if (list.isEmpty()) {
            return true;
        }
        for (SyntacticPattern p : list) {
            if (!isUseless(p)) {
                return false;
            }
        }
        return true;
    }
}
