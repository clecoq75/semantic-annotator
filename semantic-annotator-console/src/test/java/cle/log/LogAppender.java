package cle.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

public class LogAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

  private final static StringBuilder MESSAGES = new StringBuilder();

  @Override
  protected void append(ILoggingEvent eventObject) {
    MESSAGES.append(eventObject.toString());
  }

  public static void reset() {
      MESSAGES.setLength(0);
  }

    public static String getContent() {
        return MESSAGES.toString();
    }
}