package project.reactor.examples;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class OperatorsTest {

  OperatorsExample subject = new OperatorsExample();

  @Test
  public void testFlatMap() {
    Flux<String> names = subject.flatMap();

    StepVerifier.create(names)
        .expectNext("L", "U", "K", "E", "L", "E", "I", "A", "H", "A", "N")
        .verifyComplete();
  }
}
