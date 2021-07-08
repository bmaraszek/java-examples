package project.reactor.examples;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class FluxAndMonoExampleTest {

  private FluxAndMonoExample subject = new FluxAndMonoExample();

  @Test
  public void namesFlux() {
    // given

    // when
    var namesFlux = subject.namesFlux();

    // then
    StepVerifier.create(namesFlux)
        //.expectNextCount(3) this consumes an element, so this could be:
        //.expectNext("alex").expectNext(2)
        .expectNext("alex", "ben", "chloe")
        .verifyComplete();
  }

  @Test
  public void namesMapFlux() {

    var namesFlux = subject.namesFluxMap();

    StepVerifier.create(namesFlux)
        .expectNext("ALEX", "BEN", "CHLOE")
        .verifyComplete();
  }
}
