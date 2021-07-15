package project.reactor.examples;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class CombinatorsExampleTest {

  private CombinatorsExample subject;

  @Test
  public void shouldConcat() {
    subject = new CombinatorsExample();
    var flux = subject.exploreConcat();

    StepVerifier.create(flux)
        .expectNext("A", "B", "C", "D", "E", "F")
        .verifyComplete();
  }

  @Test
  public void shouldConcatWith() {
    subject = new CombinatorsExample();
    var flux = subject.exploreConcatWith();

    StepVerifier.create(flux)
        .expectNext("A", "B", "C", "D", "E", "F")
        .verifyComplete();
  }

  @Test
  public void shouldConcatMonoWith() {
    subject = new CombinatorsExample();
    var flux = subject.exploreMonoConcatWith();

    StepVerifier.create(flux)
        .expectNext("A", "B")
        .verifyComplete();
  }
}
