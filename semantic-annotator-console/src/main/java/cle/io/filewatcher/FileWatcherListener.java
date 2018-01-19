package cle.io.filewatcher;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

public interface FileWatcherListener {
    void pathChanged(Path path, WatchEvent.Kind<?> kind);
}
