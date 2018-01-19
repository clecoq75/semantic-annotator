package cle.nlp.tagger;

import cle.nlp.SupportedLanguages;
import cle.nlp.pattern.exceptions.InvalidPatternException;
import cle.nlp.pattern.exceptions.PatternException;
import cle.nlp.tagger.exceptions.CyclicDependencyException;
import cle.nlp.tagger.exceptions.TaggerException;
import cle.nlp.tagger.exceptions.TaggerNotFoundException;
import cle.nlp.tagger.models.Repository;
import cle.nlp.tagger.models.TaggerModel;
import cle.utils.regexp.CharSequenceWithTimeOutException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.PatternSyntaxException;

public class TaggerFactory {
    private final Map<String, Tagger> taggers = new HashMap<>();
    private final Map<String, Error> errors = new HashMap<>();
    private final SupportedLanguages language;

    public TaggerFactory(SupportedLanguages language, Repository models) {
        if (language==null) {
            throw new IllegalArgumentException("Language can't be null");
        }
        if (models==null) {
            throw new IllegalArgumentException("Models can't be null");
        }
        this.language = language;
        load(models);
    }

    public void load(Repository models) {
        for (Error error : models.getErrors()) {
            errors.put(error.getTaggerName(), error);
        }
        if (models.size()>0) {
            // Load all taggers having no dependency or already loaded dependency
            loadTaggersWithLoadedDependencies(models);
            // Collect errors
            for (int i=0; i<2; i++) {
                if (taggers.size() + errors.size() < models.size()) {
                    collectErrors(models,i);
                }
            }
        }
    }

    private void collectErrors(Repository models, int step) {
        for (String name : models.nameSet()) {
            if (!taggers.containsKey(name) && !errors.containsKey(name)) {
                TaggerModel tm = models.get(name);
                if (!collectError(name, tm.getCollection(), models, step)) {
                    collectError(name, tm.getImportRules(), models, step);
                }
            }
        }
    }

    private boolean collectError(String name, List<String> taggerList, Repository models, int step) {
        final String tagger = "Tagger '";
        for (String dep : taggerList) {
            if (!models.containsName(dep)) {
                errors.put(name, new Error(name, new TaggerNotFoundException("Unknown dependency '" + dep + "' for tagger '" + name + "'")));
                return true;
            } else if (errors.containsKey(dep)) {
                errors.put(name, new Error(name, new CyclicDependencyException(tagger + name + "' depends of tagger '" + dep + "' which has errors.")));
                return true;
            } else if (step>0) {
                errors.put(name, new Error(name, new CyclicDependencyException(tagger + name + "' has cyclic dependencies.")));
                return true;
            }
        }
        return false;
    }

    private void loadTaggersWithLoadedDependencies(Repository models) {
        int loaded;
        do {
            loaded = 0;
            for (Entry<String, TaggerModel> entry : models.entrySet()) {
                String name = entry.getKey();
                if (!taggers.containsKey(name) && !errors.containsKey(name)) {
                    loaded += loadIfDependenciesAlreadyLoaded(name, entry.getValue());
                }
            }
        }
        while (loaded>0);
    }

    private int loadIfDependenciesAlreadyLoaded(String name, TaggerModel tm) {
        int loaded = 0;
        if ((tm.getImportRules().isEmpty() || taggers.keySet().containsAll(tm.getImportRules()))
                && (tm.getCollection().isEmpty() || taggers.keySet().containsAll(tm.getCollection()))) {
            try {
                Tagger tagger = Tagger.load(language, name, tm, this);
                if (!tagger.isCollectionOnly()) {
                    tagger.validate();
                }
                taggers.put(name, tagger);
                loaded++;
            } catch (PatternSyntaxException | TaggerException | PatternException | InvalidPatternException | CharSequenceWithTimeOutException e) {
                errors.put(name, new Error(name,e));
            }
        }
        return loaded;
    }

    public Collection<Error> getErrors() {
        return errors.values();
    }

    public int size() {
        return taggers.size();
    }

    public Collection<Tagger> getTaggers() {
        return taggers.values();
    }

    public Tagger get(String dep) throws TaggerNotFoundException {
        Tagger result = taggers.get(dep);
        if (result==null) {
            throw new TaggerNotFoundException("Tagger '"+dep+"' does not exist.");
        }
        return result;
    }

    public SupportedLanguages getLanguage() {
        return language;
    }
}
