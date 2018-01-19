package cle.io.filewatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.*;

public class FileWatcher implements Runnable, FileWatcherListener, Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileWatcher.class);

    private boolean running = true;
    private final Path path;
    private final WatchService watchService;
    private final FileWatcherListener listener;
    private final Map<String,FileWatcher> subDirectoryWatchers = new HashMap<>();

    public FileWatcher(Path path, FileWatcherListener listener) throws IOException {
        this.path = path;
        this.listener = listener;

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(this.path)) {
            for (Path pathChild : directoryStream) {
                addSubFileMatcherIfDirectory(pathChild);
            }
        }

        watchService = FileSystems.getDefault().newWatchService();
        path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.setName("FileWatch-" + path);
        t.start();
    }

    private void addSubFileMatcherIfDirectory(Path dir) throws IOException {
        addSubFileMatcherIfDirectory(dir, subDirectoryWatchers, this);
    }

    static void addSubFileMatcherIfDirectory(Path dir, Map<String,FileWatcher> subDirectoryWatchers,
                                             FileWatcher fileWatcher) throws IOException {
        if (dir.toFile().isDirectory()) {
            Path fileName = dir.getFileName();
            if (fileName!=null) {
                subDirectoryWatchers.put(fileName.toString(), new FileWatcher(dir, fileWatcher));
            }
        }
    }

    @Override
    public void close() throws IOException {
        running = false;
        watchService.close();
        for (FileWatcher subDirectoryWatcher : subDirectoryWatchers.values()) {
            subDirectoryWatcher.close();
        }
    }

    private void fireChange(Path path, WatchEvent.Kind<?> kind) {
        listener.pathChanged(path, kind);
    }

    @Override
    public void pathChanged(Path path, WatchEvent.Kind<?> kind) {
        fireChange(path, kind);
    }

    private void fireChange(WatchEvent<?> event) throws IOException {
        WatchEvent.Kind<?> kind = event.kind();
        if (kind != OVERFLOW) {
            Path changedPath = path.resolve((Path)event.context());
            if (kind==ENTRY_CREATE) {
                addSubFileMatcherIfDirectory(changedPath);
            } else if (kind==ENTRY_DELETE) {
                Path fileName = ((Path)event.context()).getFileName();
                if (fileName!=null) {
                    String key = fileName.toString();
                    if (subDirectoryWatchers.containsKey(key)) {
                        subDirectoryWatchers.get(key).close();
                        subDirectoryWatchers.remove(key);
                    }
                }
            }
            fireChange(changedPath, kind);
        }
    }

    private void watch() throws IOException, InterruptedException {
        try {
            final WatchKey wk = watchService.take();
            for (WatchEvent<?> event : wk.pollEvents()) {
                fireChange(event);
            }

            boolean valid = wk.reset();
            if (!valid) {
                LOGGER.warn("Key has been unregistered");
            }
        }
        catch (ClosedWatchServiceException e) {
            LOGGER.trace("Watch service has been closed.");
        }
    }

    @Override
    public void run() {
        try {
            while (running) {
                watch();
            }
        }
        catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        catch (IOException e) {
            LOGGER.error("An error occurred while watching for file modifications : {}", e.getMessage(), e);
        }
    }
}
