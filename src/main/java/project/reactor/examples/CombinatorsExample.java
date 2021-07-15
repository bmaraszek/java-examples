package project.reactor.examples;

import java.time.Duration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CombinatorsExample {

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  Using concat() - a static method in Flux, and concatWith() - an instance method in Flux & Mono
  //
  //  Concatenation of Reactive Streams happens in sequence: first one is subscribed and completes,
  //  second one is subscribed after that and then completes
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Flux<String> concat() {
    var abcFlux = Flux.just("A", "B", "C");
    var defFlux = Flux.just("D", "E", "F");

    return Flux.concat(abcFlux, defFlux).log();
  }

  public Flux<String> concatWith() {
    var abcFlux = Flux.just("A", "B", "C");
    var defFlux = Flux.just("D", "E", "F");

    return abcFlux.concatWith(defFlux).log();
  }

  public Flux<String> concatWithMono() {
    var aMono = Mono.just("A");
    var bMono = Mono.just("B");

    return aMono.concatWith(bMono).log();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  Using merge() and mergeWith() we combine two Publishers into one.
  //
  //  merge() - static method in Flux
  //  mergeWIth - instance method in Flux and Mono
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Flux<String> merge() {
    var abcFlux = Flux.just("A", "B", "C", "D", "E", "F")
        .delayElements(Duration.ofMillis(35));

    var defFlux = Flux.just("G", "H", "I", "J")
        .delayElements(Duration.ofMillis(100));

    // the merge happens in an interleave fashion
    // this will be: "A", "B", "G", "C", "D", "E", "H", "E", "F", "J"
    // if two fluxes emit at the same time, the result will be non-deterministic
    return Flux.merge(abcFlux, defFlux).log();
  }

  public Flux<String> mergeMono() {
    var aMono = Mono.just("A");
    var bMono = Mono.just("B");

    return aMono.mergeWith(bMono).log();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  Using mergeSequential() combines two Publishers into one.
  //    - Both Publishers are subscribed at the same time
  //    - Publishers are subscribed eagerly
  //
  //  mergeSequential() - static method in Flux
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Flux<String> mergeSequential() {
    var abcFlux = Flux.just("A", "B", "C");
    var defFlux = Flux.just("D", "E", "F");

    // subscription occurs at the same time but the result is sequential
    return Flux.mergeSequential(abcFlux, defFlux).log();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  Using zip() merges two Publishers using a combinator function
  //
  //  zip() - static method in Flux. Can merge 2 to 8 Publishers.
  //          Finishes when one of the Publishers completes.
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Flux<String> zip() {
    var abcFlux = Flux.just("A", "B", "C");
    var defFlux = Flux.just("D", "E", "F", "G"); // no pair - no output

    // subscription occurs at the same time but the result is sequential
    return Flux.zip(abcFlux, defFlux, (first, second) -> first + "-" + second).log();
  }

  public Flux<String> zip4() {
    var abcFlux = Flux.just("A", "B", "C");
    var defFlux = Flux.just("D", "E", "F");
    var flux3 = Flux.just("1", "2", "3");
    var flux4 = Flux.just("4", "5", "6");

    return Flux.zip(abcFlux, defFlux, flux3, flux4) // returns a Tuple of 4 elems
        // the Tuple needs to be mapped:
        .map(tuple4 -> tuple4.getT1() + tuple4.getT2() + tuple4.getT3() + tuple4.getT4());
  }

  public Flux<String> zipFluxWithMono() {
    var aMono = Mono.just("A");
    var bFlux = Flux.just("B", "C");

    return bFlux.zipWith(aMono, (first, second) -> first + "-" + second).log();
  }

  public Mono<String> zipMonoWithMono() {
    var aMono = Mono.just("A");
    var bMono = Mono.just("B");

    return aMono.zipWith(bMono)
        .map(tuple2 -> tuple2.getT1() + "-" + tuple2.getT2())
        // same as:
        // aMono.zipWith(bMono, (a, b) -> a + "-" + b).log();
        .log();
  }
}
