package cle.io.filewatcher;

import java.io.File;
import java.util.Set;

public interface BasicFileWatcherListener {
    void modifiedFiles(Set<File> files);
}
