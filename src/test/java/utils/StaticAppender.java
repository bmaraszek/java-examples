package utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is a test log appender meant for testing so that we do not have to inject a mock logger.
 * Instead, we can use this appender and check the in-memory list of logging events.
 * This implementation uses a ThreadLocal in order to enable parallel test execution.
 */
public class StaticAppender extends AppenderBase<ILoggingEvent> {
  static ThreadLocal<List<ILoggingEvent>> threadLocal = new ThreadLocal<>();

  @Override
  public void append(ILoggingEvent e) {
    List<ILoggingEvent> events = threadLocal.get();
    if (events == null) {
      events = new ArrayList<>();
      threadLocal.set(events);
    }
    events.add(e);
  }

  public static List<ILoggingEvent> getAllEvents() {
    return threadLocal.get();
  }

  public static void clearEvents() {
    threadLocal.remove();
  }

  /**
   * Get all logging events.
   *
   * @param level TRACE, DEBUG, INFO, WARN, ERROR
   * @return a list of logging events
   */
  public static List<ILoggingEvent> getAll(Level level) {
    return getAllEvents()
        .stream()
        .filter(e -> e.getLevel().equals(level))
        .collect(Collectors.toList());
  }

  /**
   * Checks if a logging statement containing given string has been logged exactly once.
   *
   * @param message a fragment of a logging message
   * @return true when a logging statement has been logged exactly once
   */
  public static boolean hasAMessageContaining(String message) {
    return hasMultipleMessagesContaining(message, 1);
  }

  /**
   * Checks if an error logging statement containing given string has been logged exactly once.
   *
   * @param message a fragment of a logging message
   * @return true when an error logging statement has been logged exactly once
   */
  public static boolean hasAnErrorContaining(String message) {
    return hasMultipleErrorsContaining(message, 1);
  }

  /**
   * Returns if exactly n messages containing given text have been logged.
   *
   * @param message a fragment of a logging message
   * @return true when an error logging statement has been logged exactly n times
   */
  public static boolean hasMultipleErrorsContaining(String message, int count) {
    return getAll(Level.ERROR)
        .stream()
        .filter(e -> e.getFormattedMessage().contains(message))
        .count() == count;
  }

  /**
   * Returns if exactly n messages containing given text have been logged.
   *
   * @param message a fragment of a logging message
   * @return true when a logging statement has been logged exactly n times
   */
  public static boolean hasMultipleMessagesContaining(String message, int count) {
    return getAllEvents()
        .stream()
        .filter(e -> e.getFormattedMessage().contains(message))
        .count() == count;
  }
}

