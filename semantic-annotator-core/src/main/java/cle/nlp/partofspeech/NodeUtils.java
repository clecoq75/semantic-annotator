package cle.nlp.partofspeech;

import cle.nlp.morphology.MorphologicalProperties;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static cle.nlp.morphology.MorphologicalProperties.*;
import static cle.utils.Ansi.*;

public final class NodeUtils {

    private NodeUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static String spaces(int length) {
        StringBuilder sb = new StringBuilder(length*2);
        for (int i=0; i<length; i++) {
            sb.append("  ");
        }
        return sb.toString();
    }

    public static StringBuilder printNode(Node node, int offset) {
        String prefix = spaces(offset);
        StringBuilder sb = new StringBuilder();

        sb.append(prefix).append(white(node.getText())).append(yellow("@", false));

        if (node.isGatheringNode()) {
            sb.append(cyan(node.getPartOfSpeech()));
        } else  {
            sb.append(blue(node.getPartOfSpeech()));
        }

        MorphologicalProperties m = node.getMorphologicalProperties();
        if (m!=null) {
            sb.append(printMorphologicalProperties(m));
        }
        sb.append('\n');
        List<Node> children = node.getChildren();
        if (children!=null) {
            for (Node child : children) {
                sb.append(printNode(child, offset+1));
            }
        }
        return sb;
    }

    private static StringBuilder printMorphologicalProperties(MorphologicalProperties m) {
        final String[] propertyKeys = new String[] { LEMMA, TMP, MODE, PERS, GENDER, NB };

        StringBuilder sb = new StringBuilder();
        sb.append(purple("["));
        boolean hasProperties = false;
        for (String key : propertyKeys) {
            String value = m.asProperties().get(key);
            if (!StringUtils.isBlank(value)) {
                if (hasProperties) {
                    sb.append(purple("; "));
                }
                sb.append(purple(key+"=")).append(blue(value));
                hasProperties = true;
            }
        }
        if (!hasProperties) {
            return new StringBuilder();
        }
        else {
            sb.append(purple("]"));
            return sb;
        }
    }

    public static StringBuilder printNode(Node node) {
        return printNode(node, 1);
    }
}
