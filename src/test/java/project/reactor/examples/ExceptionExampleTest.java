package project.reactor.examples;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import udemy.learnreactiveprogramming.exception.ReactorException;
import utils.StaticAppender;

public class ExceptionExampleTest {

  private ExceptionExample subject;

  @BeforeEach
  public void setup() {
    subject = new ExceptionExample();
    StaticAppender.clearEvents();
  }

  @AfterEach
  public void cleanup() {
    StaticAppender.clearEvents();
  }

  @Test
  @DisplayName("Should throw exception")
  public void shouldThrowException() {
    var flux = subject.throwException();

    StepVerifier.create(flux)
        .expectNext("A", "B", "C")
        //        .expectError(RuntimeException.class);
        .expectErrorMatches(err -> err.getMessage().contains("Exception Occurred"))
        .verify();
  }

  @Test
  @DisplayName("Should return a fallback value on error")
  public void shouldReturnFallbackValueOnError() {
    var flux = subject.onErrorReturn();

    StepVerifier.create(flux)
        .expectNext("A", "B", "C")
        .expectNext("Fallback value")
        .verifyComplete();
  }

  @Test
  @DisplayName("Should return a fallback value on error resume")
  public void shouldReturnFallbackValueOnErrorResume() {
    var flux = subject.onErrorResume(new IllegalStateException("Illegal State!"));

    StepVerifier.create(flux)
        .expectNext("A", "B", "C", "D", "E", "F")
        .verifyComplete();

    flux = subject.onErrorResume(new RuntimeException("Runtime Exception!"));

    StepVerifier.create(flux)
        .expectNext("A", "B", "C")
        .expectError(RuntimeException.class)
        .verify();
  }

  @Test
  @DisplayName("Should return a fallback value on error continue")
  public void shouldReturnFallbackValueOnErrorContinue() {
    var flux = subject.onErrorContinue();

    StepVerifier.create(flux)
        .expectNext("A", "C")
        .verifyComplete();
  }

  @Test
  @DisplayName("Should map a stream exceptions to business exception")
  public void shouldMapError() {
    var flux = subject.onErrorMap();

    StepVerifier.create(flux)
        .expectNext("A")
        .expectError(ReactorException.class)
        .verify();
  }

  @Test
  @DisplayName("Should run a task on error")
  public void shouldDoOnError() {
    var flux = subject.doOnError();

    StepVerifier.create(flux)
        .expectNext("A")
        .expectError(RuntimeException.class)
        .verify();
  }
}
