package cle.nlp.annotator;

import cle.nlp.SemanticAnnotatorUtils;
import cle.nlp.SupportedLanguages;
import cle.nlp.annotator.exceptions.SemanticAnnotatorException;
import cle.nlp.pattern.exceptions.InvalidPatternException;
import cle.nlp.pattern.exceptions.PatternException;
import cle.nlp.tagger.Error;
import cle.nlp.tagger.Tag;
import cle.nlp.tagger.exceptions.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import static cle.nlp.SemanticAnnotatorUtils.readText;
import static cle.nlp.SupportedLanguages.EN;
import static cle.nlp.SupportedLanguages.FR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SemanticAnnotatorTest {
    private SemanticAnnotator frenchAnnotator;

    public static File getTaggerFolder(String taggerName) {
        File resources = new File(SemanticAnnotatorTest.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        return new File(resources, "semanticannotators/"+taggerName);
    }

    public static SemanticAnnotator load(String taggerName, SupportedLanguages language) throws FileNotFoundException, SemanticAnnotatorException {
        return new SemanticAnnotator(language, getTaggerFolder(taggerName).getAbsolutePath());
    }

    private SemanticAnnotator getFrenchTagger() throws FileNotFoundException, SemanticAnnotatorException {
        if (frenchAnnotator==null) {
            frenchAnnotator = load("taggers-fr", FR);
        }
        return frenchAnnotator;
    }

    @Test
    public void test_simple_text() throws FileNotFoundException, SemanticAnnotatorException {
        assertEquals(SemanticAnnotatorUtils.toTagSet("emotionLove"),
                getFrenchTagger().getTags("il était terriblement amoureux d'elle"));
    }

    @Test
    public void test_simple_text_with_specific_tagger() throws TaggerException, FileNotFoundException, SemanticAnnotatorException {
        assertEquals(SemanticAnnotatorUtils.toTagSet("emotionLove"),
                getFrenchTagger().getTags("il était terriblement amoureux d'elle", "emotion"));
    }

    @Test
    public void test_large_text() throws IOException, TaggerException, SemanticAnnotatorException {
        assertEquals(SemanticAnnotatorUtils.toTagSet("emotionLove"),
                getFrenchTagger().getTags(readText("zola-au_bonheur_des_dames_CH1.txt"), "emotion"));
    }

    @Test
    public void test_large_text_with_full_analysis() throws IOException, TaggerException, SemanticAnnotatorException {
        Collection<Tag> tags = getFrenchTagger().getTags(readText("zola-au_bonheur_des_dames_CH1.txt"), "emotion", true);
        assertEquals(SemanticAnnotatorUtils.toTagList("emotionLove", "emotionLove", "emotionLove"), tags);
    }

    @Test(expected = TaggerNotFoundException.class)
    public void test_invalid_tagger() throws TaggerException, FileNotFoundException, SemanticAnnotatorException {
        getFrenchTagger().getTags("It should fail", "gastonLapoigne", true);
    }

    @Test
    public void test_empty() throws FileNotFoundException, SemanticAnnotatorException {
        load("tagger-empty", FR);
    }

    @Test
    public void test_count() throws FileNotFoundException, SemanticAnnotatorException {
        SemanticAnnotator s = load("taggers-fr", FR);
        assertEquals(15, s.getTaggersCount());
    }

    @Test(expected = TaggerNotFoundException.class)
    public void test_check_non_existing_tagger() throws FileNotFoundException, SemanticAnnotatorException, TaggerNotFoundException {
        SemanticAnnotator s = load("taggers-fr", FR);
        s.checkTagger("pikotiLaBougnette");
    }

    @Test
    public void test_existing_tagger() throws FileNotFoundException, SemanticAnnotatorException, TaggerNotFoundException {
        SemanticAnnotator s = load("taggers-fr", FR);
        s.checkTagger("smallDog");
    }

    @Test
    public void test_json_error() throws FileNotFoundException {
        try {
            load("tagger-with-json-error", FR);
            fail("An exception must be thrown.");
        }
        catch (SemanticAnnotatorException e) {
            Collection<Error> errors = e.getErrors();
            assertEquals(1, errors.size());
            assertEquals(JsonMappingException.class, errors.iterator().next().getException().getClass());
        }
    }

    @Test
    public void test_cyclic_dependencies() throws FileNotFoundException {
        try {
            load("tagger-with-cyclic-dependencies", FR);
            fail("An exception must be thrown.");
        }
        catch (SemanticAnnotatorException e) {
            Collection<Error> errors = e.getErrors();
            assertEquals(2, errors.size());
            Iterator<Error> iterator = errors.iterator();
            assertEquals(CyclicDependencyException.class, iterator.next().getException().getClass());
            assertEquals(CyclicDependencyException.class, iterator.next().getException().getClass());
        }
    }

    @Test
    public void test_invalid_pattern() throws FileNotFoundException {
        try {
            load("tagger-with-invalid-pattern", FR);
            fail("An exception must be thrown.");
        }
        catch (SemanticAnnotatorException e) {
            Collection<Error> errors = e.getErrors();
            assertEquals(1, errors.size());
            Error err = errors.iterator().next();
            assertEquals(InvalidPatternException.class, err.getException().getClass());
        }
    }

    @Test
    public void test_invalid_substitution1() throws FileNotFoundException {
        try {
            load("tagger-with-invalid-substitution1", FR);
            fail("An exception must be thrown.");
        }
        catch (SemanticAnnotatorException e) {
            Collection<Error> errors = e.getErrors();
            assertEquals(1, errors.size());
            Error err = errors.iterator().next();
            assertEquals(TaggerException.class, err.getException().getClass());
            assertEquals(PatternException.class, err.getException().getCause().getClass());
        }
    }

    @Test
    public void test_invalid_substitution2() throws FileNotFoundException {
        try {
            load("tagger-with-invalid-substitution2", FR);
            fail("An exception must be thrown.");
        }
        catch (SemanticAnnotatorException e) {
            Collection<Error> errors = e.getErrors();
            assertEquals(1, errors.size());
            Error err = errors.iterator().next();
            assertEquals(TaggerException.class, err.getException().getClass());
            assertEquals(PatternException.class, err.getException().getCause().getClass());
        }
    }

    @Test
    public void test_invalid_substitution3() throws FileNotFoundException {
        try {
            load("tagger-with-invalid-substitution3", FR);
            fail("An exception must be thrown.");
        }
        catch (SemanticAnnotatorException e) {
            Collection<Error> errors = e.getErrors();
            assertEquals(1, errors.size());
            Error err = errors.iterator().next();
            assertEquals(InvalidSubstitutionException.class, err.getException().getClass());
        }
    }

    @Test
    public void test_empty_unit_tests() throws FileNotFoundException {
        try {
            load("tagger-empty-unit-tests", FR);
            fail("An exception must be thrown.");
        }
        catch (SemanticAnnotatorException e) {
            Collection<Error> errors = e.getErrors();
            assertEquals(1, errors.size());
            Error err = errors.iterator().next();
            assertEquals(NoUnitTestsValidationException.class, err.getException().getClass());
        }
    }

    @Test
    public void test_no_unit_tests() throws FileNotFoundException {
        try {
            load("tagger-no-unit-tests", FR);
            fail("An exception must be thrown.");
        }
        catch (SemanticAnnotatorException e) {
            Collection<Error> errors = e.getErrors();
            assertEquals(1, errors.size());
            Error err = errors.iterator().next();
            assertEquals(NoUnitTestsValidationException.class, err.getException().getClass());
        }
    }

    @Test
    public void test_empty_samples_on_rule() throws FileNotFoundException {
        try {
            load("tagger-empty-samples-on-rule", FR);
            fail("An exception must be thrown.");
        }
        catch (SemanticAnnotatorException e) {
            Collection<Error> errors = e.getErrors();
            assertEquals(1, errors.size());
            Error err = errors.iterator().next();
            assertEquals(MissingRuleValidationSamplesException.class, err.getException().getClass());
        }
    }

    @Test
    public void test_no_samples_on_rule() throws FileNotFoundException {
        try {
            load("tagger-no-samples-on-rule", FR);
            fail("An exception must be thrown.");
        }
        catch (SemanticAnnotatorException e) {
            Collection<Error> errors = e.getErrors();
            assertEquals(1, errors.size());
            Error err = errors.iterator().next();
            assertEquals(MissingRuleValidationSamplesException.class, err.getException().getClass());
        }
    }

    @Test
    public void test_unknown_dependency() throws FileNotFoundException {
        try {
            load("tagger-unknown-dependency", FR);
            fail("An exception must be thrown.");
        }
        catch (SemanticAnnotatorException e) {
            Collection<Error> errors = e.getErrors();
            assertEquals(1, errors.size());
            Error err = errors.iterator().next();
            assertEquals(TaggerNotFoundException.class, err.getException().getClass());
        }
    }

    @Test
    public void test_cyclic_dependency() throws FileNotFoundException {
        try {
            load("tagger-cyclic-dependency", FR);
            fail("An exception must be thrown.");
        }
        catch (SemanticAnnotatorException e) {
            Collection<Error> errors = e.getErrors();
            assertEquals(3, errors.size());
            for (Error err : errors) {
                assertEquals(CyclicDependencyException.class, err.getException().getClass());
            }
        }
    }

    @Test
    public void test_collection_with_unknown_tagger() throws FileNotFoundException {
        try {
            load("collection-with-unknown-tagger", FR);
            fail("An exception must be thrown.");
        }
        catch (SemanticAnnotatorException e) {
            Collection<Error> errors = e.getErrors();
            assertEquals(1, errors.size());
            for (Error err : errors) {
                assertEquals(TaggerNotFoundException.class, err.getException().getClass());
            }
        }
    }

    @Test
    public void test_regex_timeout_after_load() throws FileNotFoundException, SemanticAnnotatorException {
        SemanticAnnotator sem = load("tagger-with-pattern-timeout2", FR);
        assertEquals(0, sem.getTags("00000000000000000000000000").size());
    }

    @Test
    public void test_english() throws FileNotFoundException, SemanticAnnotatorException {
        SemanticAnnotator sem = load("tagger-en", EN);
        assertEquals(SemanticAnnotatorUtils.toTagSet("smallDog"),
                sem.getTags("I really hate this little dog !!"));
    }

    @Test
    public void test_pattern_optimization_but_substitutions() throws FileNotFoundException, SemanticAnnotatorException {
        SemanticAnnotator sem = load("tagger-with-non-optimized-pattern-but-substitutions", EN);
        assertEquals(SemanticAnnotatorUtils.toTagSet("smallDog"),
                sem.getTags("I really hate this little dog !!"));
    }

    @Test
    public void test_pattern_optimization() throws FileNotFoundException, SemanticAnnotatorException {
        SemanticAnnotator sem = load("tagger-with-non-optimized-pattern", EN);
        assertEquals(SemanticAnnotatorUtils.toTagSet("smallDog"),
                sem.getTags("I really hate this little dog !!"));
    }
}
