package cle.io.filewatcher;

import cle.FileSystemTestUtils;
import cle.utils.Sandman;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class BasicFileWatcherTest {
    private static File watchedDir;
    private static final long watcherPeriod = 100L;
    private static final long sleepPeriod = 200L;
    private static final long sleepBeforeAction = 50L;

    @BeforeClass
    public static void init() throws IOException {
        watchedDir = FileSystemTestUtils.createTmpDir();
    }

    @Test
    public void testNewFile() throws IOException, InterruptedException {
        final Set<File> lastModified = new HashSet<>();

        try (BasicFileWatcher ignored = new BasicFileWatcher(Paths.get(watchedDir.getAbsolutePath()), lastModified::addAll, watcherPeriod)) {
            Sandman.sleep(sleepBeforeAction);
            File newFile = FileSystemTestUtils.createNewFile(watchedDir);
            Sandman.sleep(sleepPeriod);

            assertEquals(1, lastModified.size());
            assertTrue(lastModified.contains(newFile));
        }
    }

    @Test
    public void testTwoNewFiles() throws IOException, InterruptedException {
        final Set<File> lastModified = new HashSet<>();

        try (BasicFileWatcher ignored = new BasicFileWatcher(Paths.get(watchedDir.getAbsolutePath()), lastModified::addAll, watcherPeriod)) {
            Sandman.sleep(sleepBeforeAction);
            File newFile = FileSystemTestUtils.createNewFile(watchedDir);
            Sandman.sleep(sleepPeriod);

            assertEquals(1, lastModified.size());
            assertTrue(lastModified.contains(newFile));
            lastModified.clear();

            File newFile2 = FileSystemTestUtils.createNewFile(watchedDir);
            Sandman.sleep(sleepPeriod);

            assertEquals(1, lastModified.size());
            assertTrue(lastModified.contains(newFile2));
        }
    }

    @Test
    public void testDeleteFile() throws IOException, InterruptedException {
        final Set<File> lastModified = new HashSet<>();
        File newFile = FileSystemTestUtils.createNewFile(watchedDir);

        try (BasicFileWatcher ignored = new BasicFileWatcher(Paths.get(watchedDir.getAbsolutePath()), lastModified::addAll, watcherPeriod)) {
            Sandman.sleep(sleepBeforeAction);
            assertTrue(newFile.delete());
            Sandman.sleep(sleepPeriod);

            assertEquals(1, lastModified.size());
            assertTrue(lastModified.contains(newFile));
        }
    }

    @Test
    public void testNewDirectory() throws IOException, InterruptedException {
        final Set<File> lastModified = new HashSet<>();

        try (BasicFileWatcher ignored = new BasicFileWatcher(Paths.get(watchedDir.getAbsolutePath()), lastModified::addAll, watcherPeriod)) {
            Sandman.sleep(sleepBeforeAction * 2L);
            File newDir = new File(watchedDir, FileSystemTestUtils.generateDirName());
            assertTrue(newDir.mkdir());
            Sandman.sleep(sleepPeriod * 2L);

            assertEquals(1, lastModified.size());
            assertTrue(lastModified.contains(newDir));
        }
    }

    @Test
    public void testDeleteDirectory() throws IOException, InterruptedException {
        final Set<File> lastModified = new HashSet<>();
        File newDir = new File(watchedDir, FileSystemTestUtils.generateDirName());
        assertTrue(newDir.mkdir());

        try (BasicFileWatcher ignored = new BasicFileWatcher(Paths.get(watchedDir.getAbsolutePath()), lastModified::addAll, watcherPeriod)) {
            Sandman.sleep(sleepBeforeAction);
            assertTrue(newDir.delete());
            Sandman.sleep(sleepPeriod);

            assertEquals(1, lastModified.size());
            assertTrue(lastModified.contains(newDir));
        }
    }

    @Test
    public void testNewFileInSubDirectory() throws IOException, InterruptedException {
        final Set<File> lastModified = new HashSet<>();
        File newDir = new File(watchedDir, FileSystemTestUtils.generateDirName());
        assertTrue(newDir.mkdir());

        try (BasicFileWatcher ignored = new BasicFileWatcher(Paths.get(watchedDir.getAbsolutePath()), lastModified::addAll, watcherPeriod)) {
            Sandman.sleep(sleepBeforeAction*2L);
            File newFile = FileSystemTestUtils.createNewFile(newDir);
            Sandman.sleep(sleepPeriod*2L);

            assertEquals(1, lastModified.size());
            assertTrue(lastModified.contains(newFile));
        }
    }

    @Test
    public void testDeleteFileInSubDirectory() throws IOException, InterruptedException {
        final Set<File> lastModified = new HashSet<>();
        File newDir = new File(watchedDir, FileSystemTestUtils.generateDirName());
        assertTrue(newDir.mkdir());
        File newFile = FileSystemTestUtils.createNewFile(newDir);

        try (BasicFileWatcher ignored = new BasicFileWatcher(Paths.get(watchedDir.getAbsolutePath()), lastModified::addAll, watcherPeriod)) {
            Sandman.sleep(sleepBeforeAction);
            assertTrue(newFile.delete());
            Sandman.sleep(sleepPeriod);

            assertEquals(1, lastModified.size());
            assertTrue(lastModified.contains(newFile));
        }
    }

    @Test
    public void testNewDirInSubDirectory() throws IOException, InterruptedException {
        final Set<File> lastModified = new HashSet<>();
        File newDir = new File(watchedDir, FileSystemTestUtils.generateDirName());
        assertTrue(newDir.mkdir());

        try (BasicFileWatcher ignored = new BasicFileWatcher(Paths.get(watchedDir.getAbsolutePath()), lastModified::addAll, watcherPeriod)) {
            Sandman.sleep(sleepBeforeAction);
            File newSubDir = new File(watchedDir, FileSystemTestUtils.generateDirName());
            assertTrue(newSubDir.mkdir());

            Sandman.sleep(sleepPeriod);

            assertEquals(1, lastModified.size());
            assertTrue(lastModified.contains(newSubDir));
        }
    }

    @Test
    public void testDeleteDirInSubDirectory() throws IOException, InterruptedException {
        final Set<File> lastModified = new HashSet<>();
        File newDir = new File(watchedDir, FileSystemTestUtils.generateDirName());
        assertTrue(newDir.mkdir());

        File newSubDir = new File(watchedDir, FileSystemTestUtils.generateDirName());
        assertTrue(newSubDir.mkdir());

        try (BasicFileWatcher ignored = new BasicFileWatcher(Paths.get(watchedDir.getAbsolutePath()), lastModified::addAll, watcherPeriod)) {
            Sandman.sleep(sleepBeforeAction);
            assertTrue(newSubDir.delete());

            Sandman.sleep(sleepPeriod);

            assertEquals(1, lastModified.size());
            assertTrue(lastModified.contains(newSubDir));
        }
    }

    @Test
    public void test_interruptions() throws InterruptedException, IOException {
        final Set<File> lastModified = new HashSet<>();
        Thread fileWatchThread = null;
        Thread basicThread = null;
        try (BasicFileWatcher ignored = new BasicFileWatcher(Paths.get(watchedDir.getAbsolutePath()), lastModified::addAll, watcherPeriod)) {
            Sandman.sleep(sleepBeforeAction);
            for (Thread t : Thread.getAllStackTraces().keySet()) {
                if (t.getName().startsWith("FileWatch-")) {
                    fileWatchThread = t;
                }
                else if (t.getName().startsWith("BasicFileWatcher-")) {
                    basicThread = t;
                }
            }
            fileWatchThread.interrupt();
            Sandman.sleep(sleepBeforeAction);
            assertFalse(fileWatchThread.isAlive());

            basicThread.interrupt();
            Sandman.sleep(sleepBeforeAction);
            assertFalse(basicThread.isAlive());
        }
    }
}
