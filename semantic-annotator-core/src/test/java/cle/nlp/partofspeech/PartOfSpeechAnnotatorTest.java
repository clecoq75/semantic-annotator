package cle.nlp.partofspeech;

import cle.nlp.SupportedLanguages;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class PartOfSpeechAnnotatorTest {

    @AfterClass
    public static void close() {
        PartOfSpeechAnnotator.clearInstances();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { SupportedLanguages.FR, "Le véhicule roulait trop vite." },
                { SupportedLanguages.EN, "The vehicle was driving too fast." },
                { SupportedLanguages.AR, "كانت السيارة تقود بسرعة كبيرة." },
                { SupportedLanguages.DE, "Das Fahrzeug fuhr zu schnell." },
                { SupportedLanguages.ES, "El vehículo conducía demasiado rápido." },
                { SupportedLanguages.ZH, "车开得太快了。" }
        });
    }

    private SupportedLanguages supportedLanguage;
    private String text;

    public PartOfSpeechAnnotatorTest(SupportedLanguages supportedLanguage, String text) {
        this.supportedLanguage = supportedLanguage;
        this.text = text;
        this.supportedLanguage.init();
    }

    @Test
    public void test() {
        PartOfSpeechAnnotator partOfSpeechAnnotator = PartOfSpeechAnnotator.getInstance(supportedLanguage);
        partOfSpeechAnnotator.annotate(text);
        Node root = partOfSpeechAnnotator.annotate("");
        assertTrue(root!=null);
    }
}
