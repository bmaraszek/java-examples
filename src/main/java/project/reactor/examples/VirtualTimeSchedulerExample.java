package project.reactor.examples;

import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class VirtualTimeSchedulerExample {

  public Flux<String> concatMap(Duration delay) {
    log.info("concatMap()");
    return Flux.fromIterable(List.of("Luke", "Leia", "Han"))
        .map(String::toUpperCase)
        // LUKE, LEIA, HAN -> L, U, K, E, L, E, I, A, H, A, N
        .concatMap(s -> splitStringWithDelay(s, delay))
        .log();
  }

  public Flux<String> splitStringWithDelay(String name, Duration delay) {
    log.info("splitStringWithDelay({})", name);
    var charArray = name.split("");
    return Flux.fromArray(charArray).delayElements(delay);
  }
}
