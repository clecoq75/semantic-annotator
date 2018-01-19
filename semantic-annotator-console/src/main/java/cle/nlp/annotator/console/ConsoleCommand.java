package cle.nlp.annotator.console;

import cle.nlp.tagger.exceptions.TaggerNotFoundException;

import java.io.IOException;
import java.util.regex.Pattern;

import static cle.utils.Ansi.yellow;

public class ConsoleCommand {
    private static final Pattern PATTERN_HELP = Pattern.compile("(help:?)");
    private static final Pattern PATTERN_RELOAD = Pattern.compile("(reload:?)");
    private static final Pattern PATTERN_WATCH = Pattern.compile("(watch:?)");
    static final Pattern PATTERN_QUIT = Pattern.compile("(quit:?)|(exit:?)");

    private ConsoleCommand() {
        throw new IllegalStateException("Utility class");
    }

    public static String executeCommand(String command, ConsoleApp console) throws IOException, TaggerNotFoundException {
        if (command.startsWith("pos:")) {
            ConsolePos.pos(command.substring(4).trim(), console, console.getState().getLanguage());
        } else if (command.startsWith("tag:")) {
            ConsoleTag.tag(command.substring(4).trim(), console.getState().getCurrentTagger(), console, false);
        } else if (command.startsWith("lang:")) {
            console.getState().setLanguage(command.substring(5).trim());
        } else if (command.startsWith("file:")) {
            ConsoleTag.tagFile(command.substring(5).trim(), console.getState().getCurrentTagger(), console);
        } else if (command.startsWith("use:")) {
            console.getState().setCurrentTagger(command.substring(4).trim());
        } else if (PATTERN_RELOAD.matcher(command).matches()) {
            console.loadAllTaggers();
        } else if (PATTERN_HELP.matcher(command).matches()) {
            ConsoleHelp.help(2, console);
        }
        else if (PATTERN_WATCH.matcher(command).matches()) {
            ConsoleWatcher.watch(console);
        } else if (command.length()>0 && !PATTERN_QUIT.matcher(command).matches()) {
            console.println(yellow("Unknown command '"+command+"' (enter 'help:' for commands list)"));
        }

        return command;
    }
}
