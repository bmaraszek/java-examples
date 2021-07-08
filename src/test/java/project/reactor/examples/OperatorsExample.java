package project.reactor.examples;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

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

  public Flux<String> concatMap() {
    log.info("concatMap()");
    return Flux.fromIterable(List.of("Luke", "Leia", "Han"))
        .map(String::toUpperCase)
        // LUKE, LEIA, HAN -> L, U, K, E, L, E, I, A, H, A, N
        .concatMap(s -> splitStringWithDelay(s))
        .log();
  }

  public Flux<String> splitString(String name) {
    var charArray = name.split("");
    return Flux.fromArray(charArray);
  }

  public Flux<String> splitStringWithDelay(String name) {
    var charArray = name.split("");
    var rand = new Random().nextInt(10);
    return Flux.fromArray(charArray).delayElements(Duration.ofMillis(rand));
  }
}
