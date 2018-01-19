package cle.utils;

import org.apache.commons.lang3.SystemUtils;

public final class Ansi {

    private Ansi() {
        throw new IllegalStateException("Utility class");
    }

    static final String RESET = "\u001B[0m";
    static final String BLACK = "\u001B[30m";
    static final String RED = "\u001B[31m";
    static final String GREEN = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";
    static final String BLUE = "\u001B[34m";
    static final String PURPLE = "\u001B[35m";
    static final String CYAN = "\u001B[36m";
    static final String WHITE = "\u001B[37m";
    static final String BOLD = "\033[0;1m";

    private static boolean ansiMode = !SystemUtils.IS_OS_WINDOWS;

    public static void setAnsiMode(boolean ansi) {
        ansiMode = ansi;
    }

    public static String black(String txt) {
        return apply(txt, BLACK, false);
    }

    public static String black(String txt, boolean bold) {
        return apply(txt, BLACK, bold);
    }


    public static String red(String txt) {
        return apply(txt, RED, false);
    }

    public static String red(String txt, boolean bold) {
        return apply(txt, RED, bold);
    }

    public static String green(String txt) {
        return apply(txt, GREEN, false);
    }

    public static String green(String txt, boolean bold) {
        return apply(txt, GREEN, bold);
    }

    public static String yellow(String txt) {
        return apply(txt, YELLOW, false);
    }

    public static String yellow(String txt, boolean bold) {
        return apply(txt, YELLOW, bold);
    }

    public static String blue(String txt) {
        return apply(txt, BLUE, false);
    }

    public static String blue(String txt, boolean bold) {
        return apply(txt, BLUE, bold);
    }

    public static String purple(String txt) {
        return apply(txt, PURPLE, false);
    }

    public static String purple(String txt, boolean bold) {
        return apply(txt, PURPLE, bold);
    }

    public static String cyan(String txt) {
        return apply(txt, CYAN, false);
    }

    public static String cyan(String txt, boolean bold) {
        return apply(txt, CYAN, bold);
    }

    public static String white(String txt) {
        return apply(txt, WHITE, false);
    }

    public static String white(String txt, boolean bold) {
        return apply(txt, WHITE, bold);
    }

    public static String bold(String txt) {
        return apply(txt, "", true);
    }

    private static String apply(String txt, String color, boolean bold) {
        if (ansiMode) {
            String weight = bold? BOLD : "";
            return color + weight + txt + RESET;
        }
        else {
            return txt;
        }
    }
}
