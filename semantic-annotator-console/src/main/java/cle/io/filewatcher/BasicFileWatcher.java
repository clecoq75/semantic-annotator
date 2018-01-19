package cle.io.filewatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.HashSet;
import java.util.Set;

public class BasicFileWatcher implements Runnable, FileWatcherListener, Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicFileWatcher.class);

    private boolean running = true;
    private final FileWatcher watcher;
    private final long interval;
    private final Set<File> modifiedFiles = new HashSet<>();
    private final BasicFileWatcherListener listener;

    public BasicFileWatcher(Path path, BasicFileWatcherListener listener, long interval) throws IOException {
        this.listener = listener;
        this.interval = interval;
        this.watcher = new FileWatcher(path, this);

        Thread t = new Thread(this);
        t.setDaemon(true);
        t.setName("BasicFileWatcher-" + path);
        t.start();
    }

    @Override
    public void close() throws IOException {
        synchronized (this) {
            running = false;
            watcher.close();
            notifyAll();
        }
    }

    @Override
    public void pathChanged(Path path, WatchEvent.Kind<?> kind) {
        synchronized (modifiedFiles) {
            modifiedFiles.add(path.toAbsolutePath().toFile());
        }
    }

    @Override
    public void run() {
        try {
            while (running) {
                synchronized (modifiedFiles) {
                    if (!modifiedFiles.isEmpty()) {
                        listener.modifiedFiles(modifiedFiles);
                        modifiedFiles.clear();
                    }
                }
                synchronized (this) {
                    wait(interval);
                }
            }
        }
        catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }
}
