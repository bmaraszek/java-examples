package project.reactor.examples;

import java.time.Duration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class OperatorsExample {
  private static final Logger log = LoggerFactory.getLogger(OperatorsExample.class);

  /**
   * flatMap() transforms one source element to a Flux of 1 to N elements
   * E.g. "LUKE" -> Flux.just("L", "U", "K", "E")
   */
  public Flux<String> flatMap() {
    log.info("flatMap()");
    return Flux.fromIterable(List.of("Luke", "Leia", "Han"))
        .map(String::toUpperCase)
        // LUKE, LEIA, HAN -> L, U, K, E, L, E, I, A, H, A, N
        .flatMap(s -> splitString(s))
        .log();
  }

  public Flux<String> flatMapAsync() {
    log.info("flatMapAsync()");
    return Flux.fromIterable(List.of("Luke", "Leia", "Han"))
        .map(String::toUpperCase)
        // LUKE, LEIA, HAN -> L, U, K, E, L, E, I, A, H, A, N
        .flatMap(s -> splitStringWithDelay(s))
        .log();
  }

  /**
   * concatMap vs flatMap:
   * flatMap uses the merge opeartor and concatMap uses concatenation
   * as a result, concatMap preserves the order of elements -> see test
   * There's a 10ms delay before emitting each element and flatMap will be faster
   * because it's executed in parallel -> compare flatMapSequential
   */
  public Flux<String> concatMap() {
    log.info("concatMap()");
    return Flux.fromIterable(List.of("Luke", "Leia", "Han"))
        .map(String::toUpperCase)
        // LUKE, LEIA, HAN -> L, U, K, E, L, E, I, A, H, A, N
        .concatMap(s -> splitStringWithDelay(s))
        .log();
  }

  /**
   * One use case for flatMap is a function that returns a Mono of a Collection, like below
   */
  public Mono<List<String>> namesMonoFlatMap(int stringLength) {
    return Mono.just("luke")
        .map(String::toUpperCase)
        .filter(n -> n.length() > stringLength)
        .flatMap(this::splitStringMono);
  }

  /**
   * A use case for flatMapMany() is turning a function returning Mono<T> into Flux<T>
   */
  public Flux<String> namesMonoFlatMapMany(int stringLength) {
    return Mono.just("luke")
        .map(String::toUpperCase)
        .filter(n -> n.length() > stringLength)
        .flatMapMany(this::splitString);
  }

  public Flux<String> splitString(String name) {
    var charArray = name.split("");
    return Flux.fromArray(charArray);
  }

  public Flux<String> splitStringWithDelay(String name) {
    var charArray = name.split("");
    return Flux.fromArray(charArray).delayElements(Duration.ofMillis(10));
  }

  public Mono<List<String>> splitStringMono(String s) {
    String[] charArray = s.split("");
    List<String> charList = List.of(charArray);
    return Mono.just(charList);
  }
}
