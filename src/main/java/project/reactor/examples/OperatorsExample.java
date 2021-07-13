package project.reactor.examples;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class OperatorsExample {
  private static final Logger log = LoggerFactory.getLogger(OperatorsExample.class);

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  Using flatMap() for Flux and Mono.
  //
  //  flatMap() transforms one source element to a Flux of 1 to N elements
  //  E.g. "LUKE" -> Flux.just("L", "U", "K", "E")
  //
  //  For a Mono, a flatMap() will help to return a Mono of Collection e.g. Mono<List<String>>
  //  compare with flatMapMany() which will return a Flux<String>
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

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

  /*
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

  public Mono<List<String>> namesMonoFlatMap(int stringLength) {
    log.info("namesMonoFlatMap({})", stringLength);
    return Mono.just("luke")
        .map(String::toUpperCase)
        .filter(n -> n.length() > stringLength)
        .flatMap(this::splitStringMono);
  }

  /**
   * A use case for flatMapMany() is turning a function returning Mono<T> into Flux<T>
   */
  public Flux<String> namesMonoFlatMapMany(int stringLength) {
    log.info("namesMonoFlatMapMany({})", stringLength);
    return Mono.just("luke")
        .map(String::toUpperCase)
        .filter(n -> n.length() > stringLength)
        .flatMapMany(this::splitString);
  }

  public Flux<String> splitString(String name) {
    log.info("splitString({})", name);
    var charArray = name.split("");
    return Flux.fromArray(charArray);
  }

  public Flux<String> splitStringWithDelay(String name) {
    log.info("splitStringWithDelay({})", name);
    var charArray = name.split("");
    return Flux.fromArray(charArray).delayElements(Duration.ofMillis(10));
  }

  public Mono<List<String>> splitStringMono(String s) {
    log.info("splitStringMono({})", s);
    String[] charArray = s.split("");
    List<String> charList = List.of(charArray);
    return Mono.just(charList);
  }

  /*
   * Transform changes one type to another by using a Function: Publisher -> Publisher
   */
  public Flux<String> transform(int stringLength) {
    log.info("transform({})", stringLength);

    Function<Flux<String>, Flux<String>> filterMap =
        name -> name.map(String::toUpperCase).filter(s -> s.length() > stringLength);

    return Flux.fromIterable(List.of("Luke", "Leia", "Han"))
        .transform(filterMap)
        .flatMap(s -> splitString(s))
        .log();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  It is not mandatory for a data source to emit data all the time.
  //  We can use the defaultIfEmpty() or switchIfEmpty operators to provide default values.
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Flux<String> namesFluxDefaultIfEmpty(int stringLength) {
    log.info("namesFluxDefaultIfEmpty({})", stringLength);
    return Flux.fromIterable(List.of("Luke", "Leia", "Han"))
        .filter(s -> s.length() > stringLength)
        .defaultIfEmpty("default")
        .log();
  }

  public Flux<String> namesFluxSwitchIfEmpty(int stringLength) {
    log.info("namesFluxSwitchIfEmpty({})", stringLength);

    Function<Flux<String>, Flux<String>> filterMap =
        name -> name
            .map(String::toUpperCase)
            .filter(s -> s.length() > stringLength)
            .flatMap(s -> splitString(s));

    var defaultFlux = Flux.just("default").transform(filterMap);

    return Flux.fromIterable(List.of("Luke", "Leia", "Han"))
        .transform(filterMap)
        .switchIfEmpty(defaultFlux)
        .log();
  }
}
