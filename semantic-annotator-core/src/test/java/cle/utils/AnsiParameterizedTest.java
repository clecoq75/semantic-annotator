package cle.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static cle.utils.Ansi.*;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class AnsiParameterizedTest {
    private static final String TXT = "text sample";

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Collection<Object[]> parameters = new ArrayList<>();
        parameters.addAll(data(false));
        parameters.addAll(data(true));
        return parameters;
    }

    public static Collection<Object[]> data(boolean ansiMode) {
        Ansi.setAnsiMode(ansiMode);
        return Arrays.asList(new Object[][] {
                { black(TXT), ansiMode? BLACK + TXT + RESET : TXT },
                { black(TXT, true), ansiMode? BLACK + BOLD + TXT + RESET : TXT },
                { black(TXT, false), ansiMode? BLACK + TXT + RESET : TXT },
                { red(TXT), ansiMode? RED + TXT + RESET : TXT },
                { red(TXT, true), ansiMode? RED + BOLD + TXT + RESET : TXT },
                { red(TXT, false), ansiMode? RED + TXT + RESET : TXT },
                { green(TXT), ansiMode? GREEN + TXT + RESET : TXT },
                { green(TXT, true), ansiMode? GREEN + BOLD + TXT + RESET : TXT },
                { green(TXT, false), ansiMode? GREEN + TXT + RESET : TXT },
                { yellow(TXT), ansiMode? YELLOW + TXT + RESET : TXT },
                { yellow(TXT, true), ansiMode? YELLOW + BOLD + TXT + RESET : TXT },
                { yellow(TXT, false), ansiMode? YELLOW + TXT + RESET : TXT },
                { blue(TXT), ansiMode? BLUE + TXT + RESET : TXT },
                { blue(TXT, true), ansiMode? BLUE + BOLD + TXT + RESET : TXT },
                { blue(TXT, false), ansiMode? BLUE + TXT + RESET : TXT },
                { purple(TXT), ansiMode? PURPLE + TXT + RESET : TXT },
                { purple(TXT, true), ansiMode? PURPLE + BOLD + TXT + RESET : TXT },
                { purple(TXT, false), ansiMode? PURPLE + TXT + RESET : TXT },
                { cyan(TXT), ansiMode? CYAN + TXT + RESET : TXT },
                { cyan(TXT, true), ansiMode? CYAN + BOLD + TXT + RESET : TXT },
                { cyan(TXT, false), ansiMode? CYAN + TXT + RESET : TXT },
                { white(TXT), ansiMode? WHITE + TXT + RESET : TXT },
                { white(TXT, true), ansiMode? WHITE + BOLD + TXT + RESET : TXT },
                { white(TXT, false), ansiMode? WHITE + TXT + RESET : TXT },
                { bold(TXT), ansiMode? BOLD + TXT + RESET : TXT }
        });
    }

    private final String actual;
    private final String expected;

    public AnsiParameterizedTest(String actual, String expected) {
        this.actual = actual;
        this.expected = expected;
    }

    @Test
    public void test() {
        assertEquals(expected, actual);
    }
}
