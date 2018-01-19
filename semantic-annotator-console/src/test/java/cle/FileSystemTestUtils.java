package cle;

import cle.io.filewatcher.BasicFileWatcherTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class FileSystemTestUtils {
    public static File createTmpDir() throws IOException {
        Path tmp = Files.createTempDirectory(generateDirName());
        return tmp.toFile();
    }

    public static File getResourceFile(String fileName) {
        File nf = new File(BasicFileWatcherTest.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        return new File(nf, fileName);
    }

    public static File getResourceFile() {
        return getResourceFile("test.txt");
    }

    public static File createNewFile(File dir) throws IOException {
        File newFile = new File(dir, generateFileName());
        Files.copy(Paths.get(getResourceFile().getAbsolutePath()),
                Paths.get(newFile.getAbsolutePath()));
        return newFile;
    }

    public static String generateDirName() {
        return randomAlphanumeric(3) + "-" + System.currentTimeMillis() + "-" + randomAlphanumeric(8);
    }

    public static String generateFileName() {
        return "UnitTests" + "-" + System.currentTimeMillis() + "-" + randomAlphanumeric(8)+".txt";
    }
}
