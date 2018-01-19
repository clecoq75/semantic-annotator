package cle.nlp.tagger;

import cle.nlp.tagger.exceptions.InvalidSubstitutionException;
import org.junit.Test;

public class SubstitutionTest {
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_with_null_label() throws InvalidSubstitutionException {
        new Substitution(1, null);
    }

    @Test(expected = InvalidSubstitutionException.class)
    public void test_constructor_with_invalid_label() throws InvalidSubstitutionException {
        new Substitution(1, "ù*$£é3'()@");
    }

    @Test(expected = InvalidSubstitutionException.class)
    public void test_constructor_with_invalid_index() throws InvalidSubstitutionException {
        new Substitution(0, "POM");
    }
}
