package cle.utils.regexp;

public class CharSequenceWithTimeOut implements CharSequence {
    private final CharSequence original;
    private final long timeOutTimeStamp;

    public CharSequenceWithTimeOut(CharSequence original, long timeToLive) {
        this(original, timeToLive, false);
    }

    private CharSequenceWithTimeOut(CharSequence original, long timeToLive, boolean isTimestamp) {
        this.original = original;
        this.timeOutTimeStamp = isTimestamp? timeToLive : System.currentTimeMillis() + timeToLive;
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
        return new CharSequenceWithTimeOut(original.subSequence(start, end), timeOutTimeStamp, true);
    }

    private void checkTimeOut() {
        if (System.currentTimeMillis() > timeOutTimeStamp) {
            throw new CharSequenceWithTimeOutException("Regular expression timeout");
        }
    }
}
