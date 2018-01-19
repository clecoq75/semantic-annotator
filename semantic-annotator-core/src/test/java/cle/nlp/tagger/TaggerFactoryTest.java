package cle.nlp.tagger;

import cle.nlp.SupportedLanguages;
import cle.nlp.tagger.models.Repository;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaggerFactoryTest {
    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_with_null_language() {
        new TaggerFactory(null, mock(Repository.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_with_null_models() {
        new TaggerFactory(SupportedLanguages.FR, null);
    }

    @Test
    public void test_models_errors_are_collected() {
        Error error = new Error("jeanLouis", new Exception("Badaboom"));
        List<Error> errors = new ArrayList<>();
        errors.add(error);

        Repository r = mock(Repository.class);
        when(r.getErrors()).thenReturn(errors);
        when(r.size()).thenReturn(0);

        TaggerFactory t = new TaggerFactory(SupportedLanguages.FR, r);
        assertTrue(errors.containsAll(t.getErrors()));
        assertTrue(t.getErrors().containsAll(errors));
    }
}
