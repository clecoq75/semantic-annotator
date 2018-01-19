package cle.nlp;

import cle.nlp.tagger.Tag;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SemanticAnnotatorUtils {
    public static Set<Tag> toTagSet(String... values) {
        Set<Tag> result = new HashSet<>();
        for (String value : values) {
            result.add(new Tag(value));
        }
        return result;
    }

    public static List<Tag> toTagList(String... values) {
        List<Tag> result = new ArrayList<>();
        for (String value : values) {
            result.add(new Tag(value));
        }
        return result;
    }

    public static String readText(String name) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (InputStream in=SemanticAnnotatorUtils.class.getResourceAsStream("/texts/"+name);
             Reader reader=new InputStreamReader(in,"utf-8")) {
            char[] buf = new char[1024];
            int read;
            while ((read=reader.read(buf))>-1) {
                sb.append(buf, 0, read);
            }
        }
        return sb.toString();
    }
}
