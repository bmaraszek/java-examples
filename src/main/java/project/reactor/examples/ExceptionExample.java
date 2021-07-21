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
  //  There are 2 categories of exception handling operators:
  //    - Recover from Exception
  //      - onErrorReturn(), onErrorResume(), onErrorContinue()
  //    - Take an action on the exception and re-throw the exception (like try-catch)
  //      - onErrorMap(), doOnError()
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Flux<String> throwException() {
    return Flux.just("A", "B", "C")
        .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
        .concatWith(Flux.just("D"))
        .log();
  }

  public Flux<String> onErrorReturn() {
    return Flux.just("A", "B", "C")
        .concatWith(Flux.error(new IllegalStateException("Exception Occurred")))
        .onErrorReturn("Fallback value")
        .log();
  }

  public Flux<String> onErrorResume(Exception exception) {

    var recoveryFlux = Flux.just("D", "E", "F");

    return Flux.just("A", "B", "C")
        .concatWith(Flux.error(exception))
        .onErrorResume((ex) -> {
          log.error("Caught exception: ", ex);
          if (ex instanceof IllegalStateException) {
            return recoveryFlux;
          } else {
            return Flux.error(ex); // throw the exception back to the caller
          }
        })
        .log();
  }

  public Flux<String> onErrorContinue() {

    // drop the element causing the exception and continue emitting the remaining elems

    return Flux.just("A", "B", "C")
        .map(name -> {
          if (name.equals("B")) {
            throw new RuntimeException("Error!");
          }
          return name;
        })
        .onErrorContinue((ex, name) -> {
          log.error("Exception is: ", ex);
          log.info("Name is {}", name);
        })
        .log();
  }
}
