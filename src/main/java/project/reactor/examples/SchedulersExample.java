package project.reactor.examples;

import static udemy.learnreactiveprogramming.util.CommonUtil.delay;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class SchedulersExample {

  static List<String> swNames = List.of("luke", "leia", "han");
  static List<String> lotrNames = List.of("frodo", "bilbo", "sam");

  public Flux<String> explorePublishOn() {
    log.info("explorePublish()");
    var swNamesFlux = Flux.fromIterable(swNames)
        .publishOn(Schedulers.parallel()) // without this all events will be published on [main]
        .map(this::upperCase)
        .log();

    var lotrNamesFlux = Flux.fromIterable(lotrNames)
        .publishOn(Schedulers.parallel())
        .map(this::upperCase)
        .map( s -> { log.info("Name is {}", s); return s; })
        .log();

    /**
     * Without .publishOn(...) this will publish all values from swNames first and then all values
     * from lotrNames sequentially.
     * With .publishOn(...) the fluxes are subscribed to eagerly and both emit values
     * at the same time.
     * The thread will switch from [main] to [parallel-1] and [parallel-2]
     */
    return swNamesFlux.mergeWith(lotrNamesFlux);
  }

  public Flux<String> exploreSubscribeOn() {
    log.info("exploreSubscribeOn()");
    var swNamesFlux = getFlux(swNames)
        .subscribeOn(Schedulers.boundedElastic())
        .map(this::upperCase)
        .log();

    var lotrNamesFlux = getFlux(lotrNames)
        .subscribeOn(Schedulers.boundedElastic())
        .map(this::upperCase)
        .map( s -> { log.info("Name is {}", s); return s; })
        .log();

    return swNamesFlux.mergeWith(lotrNamesFlux);
  }

  /**
   * Let's pretend this is a part of external library and we have no control over it.
   * This will be used to demostrate subscribeOn(...)
   * @param names - list of names
   * @return a flux from a blocking operation
   */
  private Flux<String> getFlux(List<String> names) {
    log.info("getFlux()");
    return Flux.fromIterable(names)
        .map(this::upperCase);
  }

  private String upperCase(String name) {
    log.info("upperCase()");
    delay(100); // locally block the thread
    return name.toUpperCase();
  }

  public static void main(String[] args) {

  }
}
