package cle.nlp.pattern;

import cle.nlp.morphology.MorphologicalProperties;
import cle.nlp.partofspeech.Node;
import cle.nlp.pattern.exceptions.InvalidPatternException;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cle.nlp.pattern.TokenSyntacticMatcher.NO_MATCH;

public class TokenSyntacticPattern implements SyntacticPattern {
    private static class Relevance {
        int value = 0;
    }

    public static final String REGEX_TEXT = "[\\p{IsAlphabetic}0-9'\\-?._]*";
    public static final String REGEX_POS = "[A-Za-z0-9]*";
    public static final String REGEX_PROPERTIES = "(;[a-zA-Z]+=[a-zA-Z0-9]+)*";
    public static final String REGEX_FULL = REGEX_TEXT+"([:@]"+REGEX_POS+""+REGEX_PROPERTIES+")?";
    public static final String REGEX_FULL_WITH_GROUP = "^("+REGEX_TEXT+")([:@]("+REGEX_POS+")("+REGEX_PROPERTIES+"))?$";
    public static final Pattern PATTERN = Pattern.compile(REGEX_FULL_WITH_GROUP);

    private String text;
    private String pos;
    private MorphologicalProperties morphologicalProperties;
    private SyntacticPattern parent;

    public TokenSyntacticPattern(String expression, SyntacticPattern parent) throws InvalidPatternException {
        this.parent = parent;
        Matcher m = PATTERN.matcher(expression);
        if (m.matches()) {
            text = StringUtils.trimToNull(m.group(1));
            pos = StringUtils.trimToNull(m.group(3));
            String properties = StringUtils.trimToNull(m.group(4));
            if (properties!=null) {
                HashMap<String,String> props = new HashMap<>();
                String[] parts = properties.split(";");
                for (String part : parts) {
                    int idx = part.indexOf('=');
                    if (idx>-1) {
                        props.put(part.substring(0, idx), part.substring(idx + 1));
                    }
                }
                morphologicalProperties = new MorphologicalProperties(props);
            }
        }
        else {
            throw new InvalidPatternException("Invalid token expression : "+expression);
        }
    }

    @Override
    public SyntacticMatcher matcher(Node node, int fromIndex) {
        return matcher(node);
    }

    @Override
    public SyntacticMatcher matcher(Node node) {
        Relevance relevance = new Relevance();
        relevance.value++;
        if (text!=null && !text.equals(node.getRepresentation()) && !text.equals(node.getLowerCaseText())) {
            return NO_MATCH;
        }
        else if (pos!=null && !pos.equals(node.getPartOfSpeech())) {
            return NO_MATCH;
        }
        else if (morphologicalProperties!=null) {
            Map<String,String> matchProperties = morphologicalProperties.asProperties();
            if (node.getMorphologicalProperties()==null
                || !isNodeMatchingProperties(node, matchProperties, relevance)) {
                return NO_MATCH;
            }
        }

        if (text!=null) {
            relevance.value++;
        }

        if (pos!=null) {
            relevance.value++;
        }

        return new TokenSyntacticMatcher(true, node, relevance.value, this);
    }

    private boolean isNodeMatchingProperties(Node node, Map<String,String> matchProperties, Relevance relevance) {
        Map<String, String> nodeProperties = node.getMorphologicalProperties().asProperties();
        for (Entry<String,String> entry : matchProperties.entrySet()) {
            if (!nodeProperties.containsKey(entry.getKey())
                || !nodeProperties.get(entry.getKey()).equals(entry.getValue())) {
                return false;
            }
            else {
                relevance.value++;
            }
        }

        return true;
    }

    @Override
    public SyntacticPattern getParent() {
        return parent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (text!=null) {
            sb.append(text);
        }
        sb.append("@");
        if (pos!=null) {
            sb.append(pos);
        }
        if (morphologicalProperties!=null) {
            for (String key : morphologicalProperties.asProperties().keySet()) {
                sb.append(';').append(key).append('=').append(morphologicalProperties.asProperties().get(key));
            }
        }
        return sb.toString();
    }
}
