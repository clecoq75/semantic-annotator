package cle.nlp.tagger;

import cle.nlp.SupportedLanguages;
import cle.nlp.partofspeech.Node;
import cle.nlp.pattern.exceptions.InvalidPatternException;
import cle.nlp.tagger.exceptions.InvalidSubstitutionException;
import cle.nlp.tagger.exceptions.TaggerException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class TaggerTest {
    @Test
    public void test() throws IOException, InvalidPatternException, InvalidSubstitutionException {
        try (InputStream in=TaggerParameterizedTest.class.getResourceAsStream("/semanticannotators/taggers-fr/noTag.json")) {
            Tagger tagger = Tagger.load(SupportedLanguages.FR, "test", in, null);
            tagger.setUnitTest(null);
            assertEquals(0, tagger.getUnitTests().size());
        }
    }

    @Test(expected = IOException.class)
    public void test_ioexception() throws IOException, InvalidPatternException, InvalidSubstitutionException {
        InputStream in  = mock(InputStream.class);
        Tagger.load(SupportedLanguages.FR, "test", in, null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void test_unsupported_encoding() throws IOException, InvalidPatternException, InvalidSubstitutionException {
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new UnsupportedEncodingException("Boom");
            }
        };
        Tagger.load(SupportedLanguages.FR, "test", in, null);
    }

    @Test(expected = JsonParseException.class)
    public void test_JsonParseException() throws IOException, InvalidPatternException, InvalidSubstitutionException {
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new JsonParseException(null,"");
            }
        };
        Tagger.load(SupportedLanguages.FR, "test", in, null);
    }

    @Test(expected = JsonMappingException.class)
    public void test_JsonMappingException() throws IOException, InvalidPatternException, InvalidSubstitutionException {
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new JsonMappingException(null,"");
            }
        };
        Tagger.load(SupportedLanguages.FR, "test", in, null);
    }

    @Test
    public void test_tag_with_null_children() throws IOException, InvalidPatternException, TaggerException {
        try (InputStream in=TaggerParameterizedTest.class.getResourceAsStream("/semanticannotators/taggers-fr/smallDog.json")) {
            Tagger tagger = Tagger.load(SupportedLanguages.FR, "smallDog", in, null);
            Node n = new Node("text", "ROOT", null);
            assertTrue(tagger.tag(n, false, true).isEmpty());
        }
    }

    @Test
    public void test_tag_with_empty_children() throws IOException, InvalidPatternException, TaggerException {
        try (InputStream in=TaggerParameterizedTest.class.getResourceAsStream("/semanticannotators/taggers-fr/smallDog.json")) {
            Tagger tagger = Tagger.load(SupportedLanguages.FR, "smallDog", in, null);
            Node n = new Node("text", "ROOT", null);
            n.addChild(new Node("other text", "SENT", null));
            n.getChildren().clear();
            assertTrue(tagger.tag(n, false, true).isEmpty());
        }
    }
}
