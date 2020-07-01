package cle.nlp.annotator;

import cle.nlp.SupportedLanguages;
import cle.nlp.annotator.exceptions.SemanticAnnotatorException;
import cle.nlp.annotator.exceptions.SemanticAnnotatorRuntimeException;
import cle.nlp.partofspeech.Node;
import cle.nlp.partofspeech.PartOfSpeechAnnotator;
import cle.nlp.tagger.Error;
import cle.nlp.tagger.Tag;
import cle.nlp.tagger.Tagger;
import cle.nlp.tagger.TaggerFactory;
import cle.nlp.tagger.exceptions.TaggerException;
import cle.nlp.tagger.exceptions.TaggerNotFoundException;
import cle.nlp.tagger.models.FileSystemRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class SemanticAnnotator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SemanticAnnotator.class);
    private TaggerFactory taggerFactory;
    private final SupportedLanguages language;

    /**
     * Build a new semantic annotator using files contained in the given directory.
     *
     * @param sourceDirectory A directory containing all taggers definition files.
     * @throws FileNotFoundException If the directory does not exist.
     * @throws SemanticAnnotatorException If any error occurred while validating taggers.
     */
    public SemanticAnnotator(SupportedLanguages language, String sourceDirectory) throws FileNotFoundException, SemanticAnnotatorException {
        this(language, new File(sourceDirectory));
    }

    /**
     * Build a new semantic annotator using files contained in the given directory.
     *
     * @param sourceDirectory A directory containing all taggers definition files.
     * @throws FileNotFoundException If the directory does not exist.
     * @throws SemanticAnnotatorException If any error occurred while validating taggers.
     */
    public SemanticAnnotator(SupportedLanguages language, File sourceDirectory) throws FileNotFoundException, SemanticAnnotatorException {
        this.language = language;
        taggerFactory = new TaggerFactory(language, new FileSystemRepository(sourceDirectory));
        if (!taggerFactory.getErrors().isEmpty()) {
            StringBuilder errors = new StringBuilder();
            for (Error error : taggerFactory.getErrors()) {
                LOGGER.error("Unable to load tagger '{}' : {}", error.getTaggerName(), error.getException().getMessage());
                errors.append("\n\t").append("Unable to load tagger '")
                        .append(error.getTaggerName()).append("' : ")
                        .append(error.getException().getMessage());
            }
            errors.insert(0, "Could not load the semantic annotator as errors occurred while loading taggers :");
            throw new SemanticAnnotatorException(errors.toString(), taggerFactory.getErrors());
        }
    }

    /**
     * Find tags on a given text using all loaded taggers.
     *
     * @param text The text to be analyzed.
     * @return A collection of unique tags.
     */
    public Collection<Tag> getTags(String text) {
        return getTags(text, taggerFactory.getTaggers(), true, false);
    }

    /**
     * Find tags on a given text using the specified taggers.
     *
     * @param text The text to be analyzed.
     * @param tagger The tagger to be used for analysis (if null or empty, all taggers will be used).
     * @return A collection of unique tags.
     */
    public Collection<Tag> getTags(String text, String tagger) throws TaggerNotFoundException {
        return getTags(text, tagger, false);
    }

    /**
     * Find tags on a given text using the specified taggers.
     *
     * @param text The text to be analyzed.
     * @param tagger The tagger to be used for analysis (if null or empty, all taggers will be used).
     * @param fullScan if false, only the first tag for each kind of tag will be returned.
     *                 Otherwise all occurrences will be returned.
     * @return A collection tags.
     */
    public Collection<Tag> getTags(String text, String tagger, boolean fullScan) throws TaggerNotFoundException {
        boolean allTaggers = StringUtils.isBlank(tagger);
        Collection<Tagger> list = allTaggers? taggerFactory.getTaggers() : Collections.singletonList(taggerFactory.get(tagger));
        return getTags(text, list, allTaggers, fullScan);
    }

    private Collection<Tag> getTags(String text, Collection<Tagger> list, boolean allTaggers, boolean fullScan) {
        Node root = null;
        Collection<Tag> allTags = fullScan? new ArrayList<>() : new HashSet<>();
        for (Tagger taggerFromList : list) {
            if (taggerFromList.generatesTags() || taggerFromList.isCollectionOnly()) {
                if (root == null) {
                    root = PartOfSpeechAnnotator.getInstance(this.language).annotate(text);
                }
                Node n = allTaggers? root.copy() : root;
                try {
                    Collection<Tag> tags = taggerFromList.tag(n, fullScan);
                    allTags.addAll(tags);
                } catch (TaggerException e) {
                    throw new SemanticAnnotatorRuntimeException("An unexpected error occurred : "+e.getMessage(), e);
                }
            }
        }
        return allTags;
    }

    /**
     * @return The loaded taggers count.
     */
    public int getTaggersCount() {
        return taggerFactory.size();
    }

    /**
     * Validate if a tagger exists.
     * @param tagger The naMessage()me of a tagger.
     * @throws TaggerNotFoundException If the tagger does not exists.
     */
    public void checkTagger(String tagger) throws TaggerNotFoundException {
        taggerFactory.get(tagger);
    }
}
