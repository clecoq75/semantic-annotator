package cle.io.filewatcher;

import cle.FileSystemTestUtils;
import cle.TestUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
            TestUtils.sleep(sleepBeforeAction);
            File newFile = FileSystemTestUtils.createNewFile(watchedDir);
            TestUtils.sleep(sleepPeriod);

            assertEquals(1, lastModified.size());
            assertTrue(lastModified.contains(newFile));
        }
    }

    @Test
    public void testTwoNewFiles() throws IOException, InterruptedException {
        final Set<File> lastModified = new HashSet<>();

        try (BasicFileWatcher ignored = new BasicFileWatcher(Paths.get(watchedDir.getAbsolutePath()), lastModified::addAll, watcherPeriod)) {
            TestUtils.sleep(sleepBeforeAction);
            File newFile = FileSystemTestUtils.createNewFile(watchedDir);
            TestUtils.sleep(sleepPeriod);

            assertEquals(1, lastModified.size());
            assertTrue(lastModified.contains(newFile));
            lastModified.clear();

            File newFile2 = FileSystemTestUtils.createNewFile(watchedDir);
            TestUtils.sleep(sleepPeriod);

            assertEquals(1, lastModified.size());
            assertTrue(lastModified.contains(newFile2));
        }
    }

    @Test
    public void testDeleteFile() throws IOException, InterruptedException {
        final Set<File> lastModified = new HashSet<>();
        File newFile = FileSystemTestUtils.createNewFile(watchedDir);

        try (BasicFileWatcher ignored = new BasicFileWatcher(Paths.get(watchedDir.getAbsolutePath()), lastModified::addAll, watcherPeriod)) {
            TestUtils.sleep(sleepBeforeAction);
            assertTrue(newFile.delete());
            TestUtils.sleep(sleepPeriod);

            assertEquals(1, lastModified.size());
            assertTrue(lastModified.contains(newFile));
        }
    }

    @Test
    public void testNewDirectory() throws IOException, InterruptedException {
        final Set<File> lastModified = new HashSet<>();

        try (BasicFileWatcher ignored = new BasicFileWatcher(Paths.get(watchedDir.getAbsolutePath()), lastModified::addAll, watcherPeriod)) {
            TestUtils.sleep(sleepBeforeAction * 2L);
            File newDir = new File(watchedDir, FileSystemTestUtils.generateDirName());
            assertTrue(newDir.mkdir());
            TestUtils.sleep(sleepPeriod * 2L);

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
            TestUtils.sleep(sleepBeforeAction);
            assertTrue(newDir.delete());
            TestUtils.sleep(sleepPeriod);

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
            TestUtils.sleep(sleepBeforeAction*2L);
            File newFile = FileSystemTestUtils.createNewFile(newDir);
            TestUtils.sleep(sleepPeriod*2L);

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
            TestUtils.sleep(sleepBeforeAction);
            assertTrue(newFile.delete());
            TestUtils.sleep(sleepPeriod);

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
            TestUtils.sleep(sleepBeforeAction);
            File newSubDir = new File(watchedDir, FileSystemTestUtils.generateDirName());
            assertTrue(newSubDir.mkdir());

            TestUtils.sleep(sleepPeriod);

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
            TestUtils.sleep(sleepBeforeAction);
            assertTrue(newSubDir.delete());

            TestUtils.sleep(sleepPeriod);

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
            TestUtils.sleep(sleepBeforeAction);
            for (Thread t : Thread.getAllStackTraces().keySet()) {
                if (t.getName().startsWith("FileWatch-")) {
                    fileWatchThread = t;
                }
                else if (t.getName().startsWith("BasicFileWatcher-")) {
                    basicThread = t;
                }
            }
            fileWatchThread.interrupt();
            TestUtils.sleep(sleepBeforeAction);
            assertFalse(fileWatchThread.isAlive());

            basicThread.interrupt();
            TestUtils.sleep(sleepBeforeAction);
            assertFalse(basicThread.isAlive());
        }
    }
}
