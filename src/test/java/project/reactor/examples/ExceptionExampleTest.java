package project.reactor.examples;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
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
  public void shouldThrowException() {
    var flux = subject.throwException();

    StepVerifier.create(flux)
        .expectNext("A", "B", "C")
        //        .expectError(RuntimeException.class);
        .expectErrorMatches(err -> err.getMessage().contains("Exception Occurred"))
        .verify();
  }
}
