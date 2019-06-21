package cle.utils.regexp;

public class CharSequenceWithTimeOut implements CharSequence {
    interface Validator {
        boolean validate();
    }

    private final CharSequence original;
    private final long timeOutTimeStamp;
    private Validator validator;

    public CharSequenceWithTimeOut(CharSequence original, long timeToLive) {
        this(original, timeToLive, false, null);
    }

    CharSequenceWithTimeOut(CharSequence original, long timeToLive, Validator validator) {
        this(original, timeToLive, false, validator);
    }

    private CharSequenceWithTimeOut(CharSequence original, long timeToLive, boolean isTimestamp, Validator validator) {
        this.original = original;
        this.timeOutTimeStamp = isTimestamp? timeToLive : System.currentTimeMillis() + timeToLive;
        this.validator = validator;
    }

    @Override
    public int length() {
        return original.length();
    }

    @Override
    public char charAt(int index) {
        checkTimeOut();
        return original.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new CharSequenceWithTimeOut(original.subSequence(start, end), timeOutTimeStamp, true, validator);
    }

    private void checkTimeOut() {
        if (System.currentTimeMillis() > timeOutTimeStamp || (validator!=null && !validator.validate())) {
            throw new CharSequenceWithTimeOutException("Regular expression timeout");
        }
    }
}
