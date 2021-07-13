package project.reactor.examples;

import java.util.List;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class OperatorsExampleTest {

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

  @Test
  public void testMonoFlatMap() {
    Mono<List<String>> listMono = subject.namesMonoFlatMap(3);

    StepVerifier.create(listMono)
        .expectNext(List.of("L", "U", "K", "E"))
        .verifyComplete();
  }

  @Test
  public void testflatMapMany() {
    var value = subject.namesMonoFlatMapMany(3);

    StepVerifier.create(value)
        .expectNext("L", "U", "K", "E")
        .verifyComplete();
  }
}
