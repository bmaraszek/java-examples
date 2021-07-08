package project.reactor.examples;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoExampleTest {

  private FluxAndMonoExample subject = new FluxAndMonoExample();

  @Test
  public void namesFlux() {
    // when
    var namesFlux = subject.namesFlux();

    // then
    StepVerifier.create(namesFlux)
        //.expectNextCount(3) this consumes an element, so this could be:
        //.expectNext("alex").expectNext(2)
        .expectNext("luke", "leia", "han")
        .verifyComplete();
  }

  @Test
  public void namesMapFlux() {

    var namesFlux = subject.namesFluxMap();

    StepVerifier.create(namesFlux)
        .expectNext("LUKE", "LEIA", "HAN")
        .verifyComplete();
  }

  @Test
  public void testImmutable() {
    Flux<String> namesFlux = subject.namesFluxImmutable();

    StepVerifier.create(namesFlux)
        .expectNext("luke", "leia", "han")
        .verifyComplete();
  }
}
