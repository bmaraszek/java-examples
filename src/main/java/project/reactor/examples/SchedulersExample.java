package project.reactor.examples;

import static udemy.learnreactiveprogramming.util.CommonUtil.delay;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class SchedulersExample {

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  Reactor comes with several default Scheduler implementations, each with its own specificity
  //  about how it manages Workers. They can be instantiated via the Schedulers factory methods.
  //  Here are rule of thumbs for their typical usage:
  //
  //    - Schedulers.immediate(): can be used as a null object for when an API
  //                              requires a Scheduler but you don’t want to change threads
  //
  //    - Schedulers.single():    is for one-off tasks that can be run on a unique ExecutorService
  //
  //    - Schedulers.parallel():  is good for CPU-intensive but short-lived tasks.
  //                              It executes N such tasks in parallel (default N == number of CPUs)
  //
  //    - Schedulers.elastic():   and Schedulers.boundedElastic() are good for more long-lived tasks
  //                              (eg. blocking IO tasks). The elastic one spawns threads on-demand
  //                              without a limit while the recently introduced boundedElastic does
  //                              the same with a ceiling on the number of created threads.
  //
  //  Each flavor of Scheduler has a default global instance returned by the above methods,
  //  but one can create new instances using the Schedulers.new*** factory methods
  //  (eg. Schedulers.newParallel("myParallel", 10)) creates a new parallel Scheduler with N = 10).
  //
  //  A common use case is to wrap a blocking call in a mono and subscribe on a different thread:
  //
  //  final Flux<String> betterFetchUrls(List<String> urls) {
  //    return Flux.fromIterable(urls)
  //        .flatMap(url ->
  //            //wrap the blocking call in a Mono
  //            Mono.fromCallable(() -> blockingWebClient.get(url))
  //                //ensure that Mono is subscribed in an boundedElastic Worker
  //                .subscribeOn(Schedulers.boundedElastic())
  //        ); //each individual URL fetch runs in its own thread!
  //  }
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////


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
        .map(s -> {
          log.info("Name is {}", s);
          return s;
        })
        .log();

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  Without .publishOn(...) this will publish all values from swNames first and then all values
    //  from lotrNames sequentially.
    //  With .publishOn(...) the fluxes are subscribed to eagerly and both emit values
    //  at the same time.
    //  The thread will switch from [main] to [parallel-1] and [parallel-2]
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////

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
        .map(s -> {
          log.info("Name is {}", s);
          return s;
        })
        .log();

    return swNamesFlux.mergeWith(lotrNamesFlux);
  }

  private Flux<String> getFlux(List<String> names) {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  Let's pretend this is a part of external library and we have no control over it.
    //  This will be used to demonstrate subscribeOn(...)
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    log.info("getFlux()");
    return Flux.fromIterable(names)
        .map(this::upperCase);
  }

  private String upperCase(String name) {
    log.info("upperCase()");
    delay(100); // locally block the thread
    return name.toUpperCase();
  }

}
