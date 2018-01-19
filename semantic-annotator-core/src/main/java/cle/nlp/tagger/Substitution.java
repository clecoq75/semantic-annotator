package cle.nlp.tagger;

import cle.nlp.tagger.exceptions.InvalidSubstitutionException;

import java.util.regex.Pattern;

public class Substitution {
    private static final Pattern SUBSTITUTIONS_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    private int groupIndex;
    private String newLabel;

    public Substitution(int groupIndex, String newLabel) throws InvalidSubstitutionException {
        if (newLabel==null) {
            throw new IllegalArgumentException("Substitution label can't be null");
        }
        else {
            if (!SUBSTITUTIONS_PATTERN.matcher(newLabel).matches()) {
                throw new InvalidSubstitutionException("Invalid label : " + newLabel);
            }
            else {
                if (groupIndex<1) {
                    throw new InvalidSubstitutionException("Invalid group index : " + groupIndex);
                }
                this.groupIndex = groupIndex;
                this.newLabel = newLabel;
            }
        }
    }

    public int getGroupIndex() {
        return groupIndex;
    }

    public String getNewLabel() {
        return newLabel;
    }
}
