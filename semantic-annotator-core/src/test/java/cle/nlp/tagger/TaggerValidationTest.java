package cle.nlp.tagger;

import cle.nlp.SupportedLanguages;
import cle.nlp.pattern.exceptions.InvalidPatternException;
import cle.nlp.pattern.exceptions.PatternException;
import cle.nlp.tagger.exceptions.*;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class TaggerValidationTest {

    @Test(expected = MissingRuleValidationSamplesException.class)
    public void test_no_samples() throws IOException, PatternException, TaggerException, InvalidPatternException {
        try (InputStream in=TaggerValidationTest.class.getResourceAsStream("/invalid/noRuleSample.json")) {
            Tagger tagger = Tagger.load(SupportedLanguages.FR, "noRuleSample", in, null);
            tagger.validate();
        }
    }

    @Test(expected = RuleValidationException.class)
    public void test_invalid_sample() throws IOException, PatternException, TaggerException, InvalidPatternException {
        try (InputStream in=TaggerValidationTest.class.getResourceAsStream("/invalid/ruleSampleNotMatches.json")) {
            Tagger tagger = Tagger.load(SupportedLanguages.FR, "ruleSampleNotMatches", in, null);
            tagger.validate();
        }
    }

    @Test(expected = RuleValidationException.class)
    public void test_invalid_sample2() throws IOException, PatternException, TaggerException, InvalidPatternException {
        try (InputStream in=TaggerValidationTest.class.getResourceAsStream("/invalid/ruleSampleNotMatches2.json")) {
            Tagger tagger = Tagger.load(SupportedLanguages.FR, "ruleSampleNotMatches2", in, null);
            tagger.validate();
        }
    }

    @Test(expected = RuleValidationException.class)
    public void test_invalid_sample3() throws IOException, PatternException, TaggerException, InvalidPatternException {
        try (InputStream in=TaggerValidationTest.class.getResourceAsStream("/invalid/ruleSampleNotMatches3.json")) {
            Tagger tagger = Tagger.load(SupportedLanguages.FR, "ruleSampleNotMatches3", in, null);
            tagger.validate();
        }
    }

    @Test(expected = UnitTestValidationException.class)
    public void test_invalid_unit_test() throws IOException, PatternException, TaggerException, InvalidPatternException {
        try (InputStream in=TaggerValidationTest.class.getResourceAsStream("/invalid/unitTestsNotMatches.json")) {
            Tagger tagger = Tagger.load(SupportedLanguages.FR, "UnitTestValidationException", in, null);
            tagger.validate();
        }
    }

    @Test(expected = NoUnitTestsValidationException.class)
    public void test_no_unit_tests_for_rules() throws IOException, PatternException, TaggerException, InvalidPatternException {
        try (InputStream in=TaggerValidationTest.class.getResourceAsStream("/invalid/noUnitTests.json")) {
            Tagger tagger = Tagger.load(SupportedLanguages.FR, "noUnitTests", in, null);
            tagger.validate();
        }
    }

    @Test(expected = NoUnitTestsValidationException.class)
    public void test_no_unit_tests_for_collection() throws IOException, PatternException, TaggerException, InvalidPatternException {
        try (InputStream in=TaggerValidationTest.class.getResourceAsStream("/invalid/noUnitTests2.json")) {
            Tagger tagger = Tagger.load(SupportedLanguages.FR, "noUnitTests2", in, null);
            tagger.validate();
        }
    }
}
