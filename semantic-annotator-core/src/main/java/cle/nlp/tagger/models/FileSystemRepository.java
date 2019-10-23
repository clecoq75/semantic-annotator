package cle.nlp.tagger.models;

import cle.nlp.tagger.Error;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FileSystemRepository implements Repository {
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final ObjectMapper YAML_MAPPER = new ObjectMapper(new YAMLFactory());
    private static final SuffixFileFilter SUFFIX_FILE_FILTER = new SuffixFileFilter(new String[] { ".json", ".yml" });

    private Map<String, TaggerModel> taggers;
    private List<Error> errors = new ArrayList<>();

    public FileSystemRepository(File directory) throws FileNotFoundException {
        if (directory==null) {
            throw new IllegalArgumentException("Directory can't be null");
        }
        else if (!directory.exists() || !directory.isDirectory()) {
            throw new FileNotFoundException("Directory '"+directory.exists()+"' doesn't exists.");
        }
        taggers = loadModels(directory);
    }

    private Map<String, TaggerModel> loadModels(File taggersDir) {
        Map<String, TaggerModel> models = new HashMap<>();
        for (File source : FileUtils.listFilesAndDirs(taggersDir, SUFFIX_FILE_FILTER, DirectoryFileFilter.DIRECTORY)) {
            loadModel(source, models);
        }
        return models;
    }

    private void loadModel(File source, Map<String, TaggerModel> models) {
        if (source.isFile()) {
            try (InputStream in = new FileInputStream(source);
                 InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                ObjectMapper mapper = source.getName().endsWith(".json")? JSON_MAPPER : YAML_MAPPER;
                TaggerModel model = mapper.readValue(reader, TaggerModel.class);
                String name = source.getName().substring(0, source.getName().lastIndexOf('.'));
                models.put(name, model);
            } catch (IOException e) {
                errors.add(new Error(source.getName(), e));
            }
        }
    }

    @Override
    public TaggerModel get(String name) {
        return taggers.get(name);
    }

    @Override
    public Set<String> nameSet() {
        return taggers.keySet();
    }

    @Override
    public Collection<TaggerModel> values() {
        return taggers.values();
    }

    @Override
    public int size() {
        return taggers.size();
    }

    @Override
    public boolean containsName(String name) {
        return taggers.containsKey(name);
    }

    @Override
    public Set<Map.Entry<String, TaggerModel>> entrySet() {
        return taggers.entrySet();
    }

    @Override
    public Collection<Error> getErrors() {
        return errors;
    }
}
