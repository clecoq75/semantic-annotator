package cle.nlp.annotator.console;

import cle.log.LogAppender;
import cle.nlp.annotator.console.exceptions.ConsoleRuntimeException;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static cle.nlp.annotator.console.Console.getBufferedReader;
import static cle.nlp.annotator.console.Console.trim;
import static org.junit.Assert.*;

public class ConsoleTest extends ConsoleTestBase {
    @Before
    public void reset() {
        LogAppender.reset();
    }

    @Test
    public void test_start() throws InterruptedException, UnsupportedEncodingException {
        InputStream in = new ByteArrayInputStream("help\nquit\n".getBytes(Charset.defaultCharset().name()));
        Console console = new Console(in, getTaggerFolder("taggers-fr").getParentFile());
        console.start();
        console.waitUntilQuit();
        assertTrue(LogAppender.getContent().contains("1 tagger loaded and validated"));
    }

    @Test
    public void test_pos() throws InterruptedException, UnsupportedEncodingException {
        InputStream in = new ByteArrayInputStream("pos: le gros chien\nquit\n".getBytes(Charset.defaultCharset().name()));
        Console console = new Console(in, getTaggerFolder("taggers-fr").getParentFile());
        console.start();
        console.waitUntilQuit();
        assertTrue(LogAppender.getContent().contains("SENT"));
    }

    @Test
    public void test_tag() throws InterruptedException, UnsupportedEncodingException {
        InputStream in = new ByteArrayInputStream("tag: le petit chien\nquit\n".getBytes(Charset.defaultCharset().name()));
        Console console = new Console(in, getTaggerFolder("taggers-fr").getParentFile());
        console.start();
        console.waitUntilQuit();
        assertTrue(LogAppender.getContent().contains("[smallDog]"));
    }

    @Test
    public void test_tag_with_specific_tagger() throws InterruptedException, UnsupportedEncodingException {
        InputStream in = new ByteArrayInputStream("use: smallDog\ntag: le petit chien\nquit\n".getBytes(Charset.defaultCharset().name()));
        Console console = new Console(in, getTaggerFolder("taggers-fr").getParentFile());
        console.start();
        console.waitUntilQuit();
        assertTrue(LogAppender.getContent().contains("[smallDog]"));
    }

    @Test
    public void test_change_lang() throws InterruptedException, UnsupportedEncodingException {
        InputStream in = new ByteArrayInputStream("lang: de\npos: Ich bin krank\nquit\n".getBytes(Charset.defaultCharset().name()));
        Console console = new Console(in, getTaggerFolder("taggers-fr").getParentFile());
        console.start();
        console.waitUntilQuit();
        assertTrue(LogAppender.getContent().contains("[de]>"));
        assertTrue(LogAppender.getContent().contains("VAFIN"));
    }

    @Test
    public void test_trim() {
        assertEquals("", trim(null));
        assertEquals("", trim(""));
        assertEquals("po", trim(" po "));
    }

    @Test
    public void test_getBufferedReader() throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream("yoopy".getBytes(StandardCharsets.UTF_8));
             BufferedReader buf = getBufferedReader(bais, "utf-8")) {
            assertNotNull(buf);
        }
    }

    @Test(expected = ConsoleRuntimeException.class)
    public void test_getBufferedReader_with_unsupportedCharset() throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream("yoopy".getBytes(StandardCharsets.UTF_8));
             BufferedReader buf = getBufferedReader(bais, "zökl6g")) {
            assertNotNull(buf);
        }
    }
}
