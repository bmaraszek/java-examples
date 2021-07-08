package project.reactor.examples;

import java.util.List;
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
    return Flux.fromIterable(List.of("Luke", "Leia", "Han"))
        .map(String::toUpperCase)
        // LUKE, LEIA, HAN -> L, U, K, E, L, E, I, A, H, A, N
        .flatMap(s -> splitString(s))
        .log();
  }

  public Flux<String> splitString(String name) {
    var charArray = name.split("");
    return Flux.fromArray(charArray);
  }
}
