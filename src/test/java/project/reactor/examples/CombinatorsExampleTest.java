package project.reactor.examples;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class CombinatorsExampleTest {

  private CombinatorsExample subject;

  @BeforeEach
  public void setup() {
    subject = new CombinatorsExample();
  }

  @Test
  public void shouldConcat() {
    var flux = subject.exploreConcat();

    StepVerifier.create(flux)
        .expectNext("A", "B", "C", "D", "E", "F")
        .verifyComplete();
  }

  @Test
  public void shouldConcatWith() {
    var flux = subject.exploreConcatWith();

    StepVerifier.create(flux)
        .expectNext("A", "B", "C", "D", "E", "F")
        .verifyComplete();
  }

  @Test
  public void shouldConcatMonoWith() {
    var flux = subject.exploreMonoConcatWith();

    StepVerifier.create(flux)
        .expectNext("A", "B")
        .verifyComplete();
  }

  @Test
  public void shouldMergeFlux() {
    var flux = subject.exploreMerge();

    StepVerifier.create(flux)
        .expectNext("A", "B", "G", "C", "D", "E", "H", "F", "I", "J")
        .verifyComplete();
  }

  @Test
  public void shouldMergeMono() {
    var flux = subject.exploreMergeMono();

    StepVerifier.create(flux)
        .expectNext("A", "B")
        .verifyComplete();
  }
}
