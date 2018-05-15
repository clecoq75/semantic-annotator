package cle.nlp.annotator.console;

import cle.nlp.annotator.SemanticAnnotator;
import cle.nlp.annotator.console.exceptions.ConsoleRuntimeException;
import cle.nlp.tagger.exceptions.TaggerException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;

import static cle.nlp.annotator.console.ConsoleCommand.PATTERN_QUIT;
import static cle.utils.Ansi.*;

public class Console implements Runnable, ConsoleApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(Console.class);

    private static final String ERROR_PREFIX = "ERROR: ";

    private ConsoleState consoleState;
    private ConsoleSemanticAnnotatorManager consoleSemanticAnnotatorManager;
    private boolean isRunning = false;
    private final java.io.Console systemConsole = System.console();
    private InputStream in;

    public Console(InputStream in, File root) {
        this.in = in;
        consoleSemanticAnnotatorManager = new ConsoleSemanticAnnotatorManager(this, root);
        consoleState = new ConsoleState(null,"", this);
    }


    public synchronized void start() {
        Thread thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    @Override
    public void loadAllTaggers() {
        consoleSemanticAnnotatorManager.load();
    }

    @Override
    public void println(String text) {
        print(text+"\n");
    }

    @Override
    public void println() {
        println("");
    }

    @Override
    public void print(String text) {
        if (systemConsole!=null) {
            systemConsole.printf(text);
        }
        else {
            LOGGER.info(text);
        }
    }

    @Override
    public void run() {
        consoleState.getLanguage().init();
        println();
        loadAllTaggers();
        println();
        println(yellow("------------------------------------------------------------------------",true));
        ConsoleHelp.help(0, this);
        println(yellow("------------------------------------------------------------------------",true));

        BufferedReader br = systemConsole==null? getBufferedReader(in, Charset.defaultCharset().name()) : null;

        if (systemConsole==null) {
            println(cyan("WARN:",true)+cyan(" console is not supported."));
        }

        String command;
        try {
            do {
                command = ConsoleCommand.executeCommand(readCommandLine(br), this);
            }
            while (!PATTERN_QUIT.matcher(command).matches());
            print(yellow("Quitting",true));
        } catch (IOException | TaggerException e) {
            println(red(ERROR_PREFIX, true) + red("Unexpected error : " + e.getMessage()));
        }
        synchronized (this) {
            isRunning = false;
            notifyAll();
        }
    }

    static BufferedReader getBufferedReader(InputStream in, String charset) {
        try {
            return new BufferedReader(new InputStreamReader(in, charset));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Unable to initialize console : {}", e.getMessage(), e);
            throw new ConsoleRuntimeException("Unable to initialize console : "+e.getMessage(), e);
        }
    }

    private String readCommandLine(BufferedReader br) throws IOException {
        String command;
        String prefix = "["+consoleState.getLanguage().name().toLowerCase()+"]";
        if (StringUtils.isNotBlank(consoleState.getCurrentTagger())) {
            prefix += " "+consoleState.getCurrentTagger();
        }
        prefix += "> ";
        if (br!=null) {
            print(bold(prefix));
        }
        if (br!=null) {
            command = trim(br.readLine());
        }
        else {
            command = systemConsole.readLine(prefix);
        }
        return command;
    }

    static String trim(String txt) {
        return txt!=null? txt.trim() : "";
    }

    public void waitUntilQuit() throws InterruptedException {
        synchronized (this) {
            while (isRunning) {
                wait();
            }
        }
    }

    @Override
    public void readLine() throws IOException {
        while ('\n'!=in.read()) {
            println();
        }
    }

    @Override
    public File getAnnotatorDirectory() {
        return consoleSemanticAnnotatorManager.getTaggerFolder();
    }

    @Override
    public SemanticAnnotator getSemanticAnnotator() {
        return consoleSemanticAnnotatorManager.getSemanticAnnotator();
    }

    @Override
    public ConsoleState getState() {
        return consoleState;
    }

    public static void main(String[] args) throws Exception {
        Console console = new Console(System.in, new File("."));
        console.start();
        console.waitUntilQuit();
    }
}
