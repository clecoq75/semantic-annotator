package cle;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.fail;

public final class TestUtils {
    private static final TestUtils INSTANCE = new TestUtils();

    public static <T> void validateConstructorNotCallable(Class<T> valueType) {
        try {
            Constructor<T> constructor = valueType.getDeclaredConstructor();
            constructor.setAccessible(true);
            if (!checkInvocationTargetException(constructor)) {
                fail("An IllegalStateException must be thrown.");
            }
        } catch (NoSuchMethodException|InstantiationException|IllegalAccessException e) {
            fail("An unexpected exception has been thrown : "+e.getClass().getSimpleName()+" ("+e.getMessage()+")");
        }
    }

    private static <T> boolean checkInvocationTargetException(Constructor<T> constructor) throws IllegalAccessException, InstantiationException {
        try {
            constructor.newInstance();
        }
        catch (InvocationTargetException e) {
            if (e.getCause()!=null && e.getCause().getClass().equals(IllegalStateException.class)) {
                return true;
            }
        }
        return false;
    }

    public static void sleep(long duration) throws InterruptedException {
        INSTANCE.pause(duration);
    }

    private void pause(long duration) throws InterruptedException {
        long timestamp = System.nanoTime() + (1000000L * duration);
        while (System.nanoTime()<timestamp) {
            synchronized (this) {
                wait(10L);
            }
        }
    }
}
