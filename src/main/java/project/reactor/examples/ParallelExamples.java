package project.reactor.examples;

import static udemy.learnreactiveprogramming.util.CommonUtil.delay;

import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

public class ParallelExamples {

  private List<String> swNames = List.of("Luke", "Leia", "Han", "Chewbacca");
  private List<String> lotrNames = List.of("Frodo", "Merry", "Pippin", "Sam");

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  Works in a similar manner to parallel streams in Java.
  //
  //  return Flux.fromIterable(list)              // return type is ParallelFlux<String>
  //             .parallel()                      // splits workload into tasks
  //             .runOn(Schedulers.parallel())    // no. of tasks == no. of CPU cores
  //             .map(this::uppercase);
  //
  //  ParallelFlux doesn't have the same API as Flux (has fewer methods e.g. missing .mergeWith()
  //  For this reason it may be preferable to use flatMap() in parallel instead
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public ParallelFlux<String> parallel() {
    return Flux.fromIterable(swNames)
        .parallel()
        .runOn(Schedulers.parallel())
        .map(this::upperCase)
        .log();
  }

  public Flux<String> parallelFlatMap() {
    return Flux.fromIterable(swNames)
        .flatMap(name -> Mono.just(name)
            .map(this::upperCase)
            .subscribeOn(Schedulers.parallel()))
        .log();
  }

  public Flux<String> parallelFlatMapWithMerge() {
    var names1 = Flux.fromIterable(swNames)
        .flatMap(name -> Mono.just(name)
            .map(this::upperCase)
            .subscribeOn(Schedulers.parallel()))
        .log();

    var names2 = Flux.fromIterable(lotrNames)
        .flatMap(name -> Mono.just(name)
            .map(this::upperCase)
            .subscribeOn(Schedulers.parallel()))
        .log();

    return names1.mergeWith(names2);
  }

  public Flux<String> parallelFlatMapSequential() {
    // This preserves the order
    return Flux.fromIterable(swNames)
        .flatMapSequential(name -> Mono.just(name)
            .map(this::upperCase)
            .subscribeOn(Schedulers.parallel()))
        .log();
  }

  private String upperCase(String name) {
    delay(1000);
    return name.toUpperCase();
  }
}
