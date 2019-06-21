package cle.utils.regexp;

import cle.utils.regexp.CharSequenceWithTimeOut.Validator;
import org.junit.Test;

import java.util.regex.Pattern;

import static cle.utils.Sandman.sleep;

public class CharSequenceWithTimeOutTest {
    @Test(expected = CharSequenceWithTimeOutException.class)
    public void test_failure() {
        final Pattern pattern = Pattern.compile("(0*)*A");
        final String text = "00000000000000000000000000";
        final CharSequenceWithTimeOut charSequenceWithTimeOut = getCharSequenceWithTimeOut(text);
        pattern.matcher(charSequenceWithTimeOut).matches();
    }

    @Test(expected = CharSequenceWithTimeOutException.class)
    public void test_failure2() {
        final Pattern pattern = Pattern.compile("(0*)*A");
        final String text = "00000000000000000000000000";
        final CharSequenceWithTimeOut charSequenceWithTimeOut = getCharSequenceWithTimeOut(text);
        pattern.matcher(charSequenceWithTimeOut.subSequence(0, text.length())).matches();
    }

    @Test
    public void test_no_failure() {
        final Pattern pattern = Pattern.compile("[a-zA-Z0-9]+(:([a-z]*&)+%?)");
        final String text = "a54d8dP4vb5G:aa&1bb:2%";
        pattern.matcher(new CharSequenceWithTimeOut(text, 200)).matches();
    }

    @Test
    public void test_no_failure2() {
        final Pattern pattern = Pattern.compile("([a-zA-Z0-9]+|popo)(:([a-z]*&)+%?)");
        final String text = "a54d8dP4vb5G:aa&1bb:2%";
        pattern.matcher(new CharSequenceWithTimeOut(text, 200).subSequence(5, 16)).matches();
    }

    private CharSequenceWithTimeOut getCharSequenceWithTimeOut(String text) {
        return new CharSequenceWithTimeOut(text, 200, getSpeedReducer(110));
    }

    private Validator getSpeedReducer(long duration) {
        return () -> {
            try {
                sleep(duration);
            } catch (InterruptedException e) {

            }
            return true;
        };
    }
}
