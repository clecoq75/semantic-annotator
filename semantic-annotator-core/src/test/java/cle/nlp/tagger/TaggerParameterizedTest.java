package cle.nlp.tagger;

import cle.nlp.SupportedLanguages;
import cle.nlp.pattern.exceptions.InvalidPatternException;
import cle.nlp.pattern.exceptions.PatternException;
import cle.nlp.tagger.exceptions.TaggerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TaggerParameterizedTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                { "/semanticannotators/taggers-fr/smallDog.json" },
                {"/semanticannotators/taggers-fr/peppaPig.json"}
        });
    }

    private final String file;

    public TaggerParameterizedTest(String file) {
        this.file = file;
    }

    @Test
    public void test() throws IOException, InvalidPatternException, TaggerException, PatternException {
        try (InputStream in=TaggerParameterizedTest.class.getResourceAsStream(file)) {
            Tagger tagger = Tagger.load(SupportedLanguages.FR, file, in, null);
            tagger.validate();
        }
    }
}
