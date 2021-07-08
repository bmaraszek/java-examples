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

  @Test
  public void testFlatMapAsync() {
    Flux<String> names = subject.flatMapAsync();

    StepVerifier.create(names)
        //.expectNext("L", "U", "K", "E", "L", "E", "I", "A", "H", "A", "N") // fail!
        .expectNextCount(11) // look at the random order
        .verifyComplete();
  }

  @Test
  public void testConcatMap() {
    Flux<String> names = subject.concatMap();

    StepVerifier.create(names)
        .expectNext("L", "U", "K", "E", "L", "E", "I", "A", "H", "A", "N") // fail!
        //.expectNextCount(11)
        .verifyComplete();
  }
}
