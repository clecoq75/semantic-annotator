package cle.utils;

public class Sandman {
    private static final long NANO_SECOND_DURATION = 1000000L;

    private Sandman() {
        throw new IllegalAccessError("Utility class");
    }

    public static void sleep(long duration) throws InterruptedException {
        Object lock = new Object();
        long waitUntil = System.nanoTime() + NANO_SECOND_DURATION * duration;
        synchronized (lock) {
            long remaining = duration;
            while (remaining>0L) {
                lock.wait(remaining);
                remaining = (waitUntil - System.nanoTime()) / NANO_SECOND_DURATION;
            }
        }
    }
}
