package cle.nlp.pattern;

public enum MatchQuantifier {
    ONCE,
    NONE_OR_ONCE,
    AT_LEAST_ONCE,
    ANY;

    public static MatchQuantifier fromChar(char c) {
        switch (c) {
            case '?':
                return NONE_OR_ONCE;
            case '+':
                return AT_LEAST_ONCE;
            case '*':
                return ANY;
            default:
                throw new IllegalArgumentException("Invalid character quantifier : " + c);
        }
    }
}
