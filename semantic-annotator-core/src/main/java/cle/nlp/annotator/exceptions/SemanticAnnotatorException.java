package cle.nlp.annotator.exceptions;

import cle.nlp.tagger.Error;

import java.util.Collection;

public class SemanticAnnotatorException extends Exception {
    private final Collection<Error> errors;

    public SemanticAnnotatorException(String message, Collection<Error> errors) {
        super(message);
        this.errors = errors;
    }

    public Collection<Error> getErrors() {
        return errors;
    }
}
