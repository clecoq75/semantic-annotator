package cle.nlp.partofspeech;

import cle.nlp.SupportedLanguages;
import cle.nlp.morphology.MorphologicalAnalyzer;
import cle.nlp.morphology.MorphologicalProperties;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.List;

public class PartOfSpeechAnnotator {
    private static final Logger LOGGER = LoggerFactory.getLogger(PartOfSpeechAnnotator.class);
    public static final String POS_ROOT = "ROOT";
    public static final String POS_SENTENCE = "SENT";

    private static final String CNLP_ANNOTATOR_KEY = "annotators";
    private static final String CNLP_ANNOTATOR_VALUE = "tokenize, ssplit, pos";
    private static final String CNLP_LANGUAGE_KEY = "tokenize.language";
    private static final String CNLP_MODEL_KEY = "pos.model";

    private static EnumMap<SupportedLanguages,PartOfSpeechAnnotator> instances = new EnumMap<>(SupportedLanguages.class);

    private StanfordCoreNLP pipelinePOS;
    private SupportedLanguages language;


    public static PartOfSpeechAnnotator getInstance(SupportedLanguages language) {
        synchronized (PartOfSpeechAnnotator.class) {
            return instances.computeIfAbsent(language, k -> {
                long duration = System.nanoTime();
                PartOfSpeechAnnotator r = new PartOfSpeechAnnotator(language);
                duration = System.nanoTime() - duration;
                LOGGER.info("Part Of Speech Annotator loaded ({}ms)", duration / 1000000L);
                return r;
            });
        }
    }

    public static void clearInstances() {
        synchronized (PartOfSpeechAnnotator.class) {
            instances.clear();
        }
    }

    private PartOfSpeechAnnotator(SupportedLanguages language) {
        LOGGER.info("Create new PartOfSpeechAnnotator for language {}", language);
        this.language = language;
        if (language==SupportedLanguages.EN) {
            pipelinePOS = new StanfordCoreNLP(PropertiesUtils.asProperties(
                    CNLP_ANNOTATOR_KEY, "tokenize, ssplit, pos, lemma"
            ));
        } else if (language==SupportedLanguages.ZH) {
            pipelinePOS = new StanfordCoreNLP(PropertiesUtils.asProperties(
                    CNLP_ANNOTATOR_KEY, "tokenize, ssplit, pos, lemma",
                    CNLP_LANGUAGE_KEY, "zh",
                    "segment.model", "edu/stanford/nlp/models/segmenter/chinese/ctb.gz",
                    "segment.sighanCorporaDict", "edu/stanford/nlp/models/segmenter/chinese",
                    "segment.serDictionary", "edu/stanford/nlp/models/segmenter/chinese/dict-chris6.ser.gz",
                    "segment.sighanPostProcessing", "true",
                    "ssplit.boundaryTokenRegex", "[.。]|[!?！？]+",
                    CNLP_MODEL_KEY, "edu/stanford/nlp/models/pos-tagger/chinese-distsim/chinese-distsim.tagger"
            ));
        } else if (language==SupportedLanguages.DE) {
            pipelinePOS = new StanfordCoreNLP(PropertiesUtils.asProperties(
                    CNLP_ANNOTATOR_KEY, CNLP_ANNOTATOR_VALUE,
                    CNLP_LANGUAGE_KEY, "de",
                    CNLP_MODEL_KEY, "edu/stanford/nlp/models/pos-tagger/german/german-fast.tagger"
            ));
        } else if (language==SupportedLanguages.ES) {
            pipelinePOS = new StanfordCoreNLP(PropertiesUtils.asProperties(
                    CNLP_ANNOTATOR_KEY, CNLP_ANNOTATOR_VALUE,
                    CNLP_LANGUAGE_KEY, "es",
                    CNLP_MODEL_KEY, "edu/stanford/nlp/models/pos-tagger/spanish/spanish.tagger"
            ));
        } else if (language==SupportedLanguages.AR) {
            pipelinePOS = new StanfordCoreNLP(PropertiesUtils.asProperties(
                    CNLP_ANNOTATOR_KEY, CNLP_ANNOTATOR_VALUE,
                    "segment.model", "edu/stanford/nlp/models/segmenter/arabic/arabic-segmenter-atb+bn+arztrain.ser.gz",
                    CNLP_LANGUAGE_KEY, "ar",
                    CNLP_MODEL_KEY, "edu/stanford/nlp/models/pos-tagger/arabic/arabic.tagger"
            ));
        } else if (language==SupportedLanguages.FR) {
            pipelinePOS = new StanfordCoreNLP(PropertiesUtils.asProperties(
                    CNLP_ANNOTATOR_KEY, CNLP_ANNOTATOR_VALUE,
                    CNLP_LANGUAGE_KEY, "fr",
                    CNLP_MODEL_KEY, "edu/stanford/nlp/models/pos-tagger/french/french.tagger"
            ));
        }
        else {
            throw new IllegalArgumentException("Unsupported language : "+language);
        }
    }

    public Node annotate(String text) {
        Node root = new Node(text, POS_ROOT, null);

        if (StringUtils.isNotBlank(text)) {
            Annotation document = new Annotation(text);
            pipelinePOS.annotate(document);

            List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
            for (CoreMap sentence : sentences) {
                Node sent = new Node(sentence.get(CoreAnnotations.TextAnnotation.class), POS_SENTENCE, null);
                sent.addChild(new Node("", "START", null));
                for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                    String word = token.get(CoreAnnotations.TextAnnotation.class);
                    String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                    String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);

                    MorphologicalProperties morphologicalProperties = MorphologicalAnalyzer.getInstance(language).getMorphologicalProperties(lemma, pos, word);
                    sent.addChild(new Node(word, pos, morphologicalProperties));
                }
                sent.addChild(new Node("", "END", null));
                root.addChild(sent);
            }
        }
        return root;
    }
}
