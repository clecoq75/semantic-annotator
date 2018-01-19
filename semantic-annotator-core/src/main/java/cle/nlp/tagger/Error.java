package cle.nlp.tagger;

import java.io.Serializable;

public class Error implements Serializable {
    private final String taggerName;
    private final Throwable exception;

    public Error(String taggerName, Throwable exception) {
        this.taggerName = taggerName;
        this.exception = exception;
    }

    public String getTaggerName() {
        return taggerName;
    }

    public Throwable getException() {
        return exception;
    }
}