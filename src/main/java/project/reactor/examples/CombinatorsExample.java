package project.reactor.examples;

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

  public Flux<String> exploreConcat() {
    var abcFlux = Flux.just("A", "B", "C");
    var defFlux = Flux.just("D", "E", "F");

    return Flux.concat(abcFlux, defFlux).log();
  }

  public Flux<String> exploreConcatWith() {
    var abcFlux = Flux.just("A", "B", "C");
    var defFlux = Flux.just("D", "E", "F");

    return abcFlux.concatWith(defFlux).log();
  }

  public Flux<String> exploreMonoConcatWith() {
    var aMono = Mono.just("A");
    var bMono = Mono.just("B");

    return aMono.concatWith(bMono).log();
  }

}
