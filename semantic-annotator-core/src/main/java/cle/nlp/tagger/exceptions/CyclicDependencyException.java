package cle.nlp.tagger.exceptions;

public class CyclicDependencyException extends TaggerException {
    public CyclicDependencyException(String message) {
        super(message);
    }
}
