package cle;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.fail;

public final class TestUtils {
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
}
