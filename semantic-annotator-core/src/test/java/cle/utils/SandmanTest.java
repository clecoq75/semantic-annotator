package cle.utils;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SandmanTest {
    private final static long MILLISECOND = 1000000;

    @Test
    public void test_short_duration() throws InterruptedException {
        test(10);
    }

    @Test
    public void test_long_duration() throws InterruptedException {
        test(500);
    }

    private void test(long duration) throws InterruptedException {
        long start = System.nanoTime();
        Sandman.sleep(duration);
        long effectiveDuration = System.nanoTime() - start;
        long gap = Math.abs((duration*MILLISECOND)-effectiveDuration);
        assertTrue("Gap "+gap+" is greater than "+ (long) 2000000, gap< (long) 2000000);
    }
}
