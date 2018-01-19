package cle.nlp.tagger.models;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class FileSystemRepositoryTest {
    @Test
    public void test() throws FileNotFoundException {
        File dir = new File(FileSystemRepositoryTest.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        dir = new File(dir, "formats");
        FileSystemRepository fsr = new FileSystemRepository(dir);
        if (!fsr.getErrors().isEmpty()) {
            fail(fsr.getErrors().iterator().next().getException().getMessage());
        }
        assertTrue(fsr.get("smallDog").getUnitTests().get(0).getTags().contains("smallDog"));
        assertTrue(fsr.get("bigDog").getUnitTests().get(0).getTags().contains("bigDog"));
        assertEquals(2, fsr.values().size());
    }

    @Test
    public void test_errors() throws FileNotFoundException {
        File dir = new File(FileSystemRepositoryTest.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        dir = new File(dir, "formats-with-errors");
        FileSystemRepository fsr = new FileSystemRepository(dir);
        assertFalse(fsr.getErrors().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_with_null() throws FileNotFoundException {
        new FileSystemRepository(null);
    }

    @Test(expected = FileNotFoundException.class)
    public void test_constructor_with_invalid_dir1() throws FileNotFoundException {
        File dir = new File(FileSystemRepositoryTest.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        dir = new File(dir, "not_a_directory.test");
        new FileSystemRepository(dir);
    }

    @Test(expected = FileNotFoundException.class)
    public void test_constructor_with_invalid_dir2() throws FileNotFoundException {
        new FileSystemRepository(new File("does_not_exists"));
    }
}
