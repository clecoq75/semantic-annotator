package cle.nlp.annotator.console;

import cle.nlp.annotator.SemanticAnnotator;

import java.io.File;
import java.io.IOException;

public class ConsoleMock implements ConsoleApp {
    private StringBuilder sb = new StringBuilder();
    private boolean reload = false;
    private long waitOnRead;
    private File dir;
    private SemanticAnnotator semanticAnnotator;
    private boolean exceptionOnRead = false;
    private ConsoleState consoleState = new ConsoleState(null,"", this);

    public ConsoleMock() {
        this(ConsoleTestBase.getTaggerFolder("taggers-fr"), 0L);
    }

    public ConsoleMock(File dir, long waitOnRead) {
        this.dir = dir;
        this.waitOnRead = waitOnRead;
    }

    public void setExceptionOnRead(boolean exceptionOnRead) {
        this.exceptionOnRead = exceptionOnRead;
    }

    @Override
    public void println(String text) {
        sb.append(text).append('\n');
    }

    @Override
    public void println() {
        sb.append('\n');
    }

    @Override
    public void print(String text) {
        sb.append(text);
    }

    @Override
    public void loadAllTaggers() {
        reload = true;
    }

    @Override
    public void readLine() throws IOException {
        if (exceptionOnRead) {
            throw new IOException("boom");
        }

        if (waitOnRead>0L) {
            synchronized (this) {
                try {
                    wait(waitOnRead);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override
    public File getAnnotatorDirectory() {
        return dir;
    }

    @Override
    public SemanticAnnotator getSemanticAnnotator() {
        return semanticAnnotator;
    }

    @Override
    public ConsoleState getState() {
        return consoleState;
    }

    public void setSemanticAnnotator(SemanticAnnotator semanticAnnotator) {
        this.semanticAnnotator = semanticAnnotator;
    }

    public String getOutput() {
        return sb.toString();
    }

    public boolean hasBeenReloaded() {
        return reload;
    }

    public void setWaitOnRead(long waitOnRead) {
        this.waitOnRead = waitOnRead;
    }
}
