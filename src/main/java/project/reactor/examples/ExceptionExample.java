package project.reactor.examples;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class ExceptionExample {

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  When an exception is encountered in a reactive flow, no other elems will be emitted.
  //  OnComplete() will never be sent. onError() will be sent instead.
  //  Any Exception will terminate a reactor stream.
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Flux<String> throwException() {
    return Flux.just("A", "B", "C")
        .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
        .concatWith(Flux.just("D"))
        .log();
  }
}
