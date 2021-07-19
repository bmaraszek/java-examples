package project.reactor.examples;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import utils.StaticAppender;

public class DoOnCallbacksExampleTest {

  private DoOnCallbacksExample subject;

  @BeforeEach
  public void setup() {
    subject = new DoOnCallbacksExample();
    StaticAppender.clearEvents();
  }

  @AfterEach
  public void cleanup() {
    StaticAppender.clearEvents();
  }

  @Test
  public void shouldExecuteDoOnNext() {
    Flux<String> flux = subject.doOnNextExample(3);

    StepVerifier.create(flux)
        .expectNext("LUKE")
        .expectNext("LEIA")
        .verifyComplete();

    Assertions.assertTrue(StaticAppender.hasAMessageContaining("doOnNext(): LUKE"));
    Assertions.assertTrue(StaticAppender.hasAMessageContaining("doOnNext(): LEIA"));
    Assertions.assertFalse(StaticAppender.hasAMessageContaining("doOnNext(): HAN"));
  }
}
