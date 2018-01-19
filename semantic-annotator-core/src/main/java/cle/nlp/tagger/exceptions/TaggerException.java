package cle.nlp.tagger.exceptions;

public class TaggerException extends Exception {
    public TaggerException(String message) {
        super(message);
    }

    public TaggerException(String message, Throwable cause) {
        super(message, cause);
    }
}
