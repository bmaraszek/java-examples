package project.reactor.examples;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import udemy.learnreactiveprogramming.exception.ReactorException;

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

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // onErrorReturn: returns a single fallback value
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Flux<String> onErrorReturn() {
    return Flux.just("A", "B", "C")
        .concatWith(Flux.error(new IllegalStateException("Exception Occurred")))
        .onErrorReturn("Fallback value")
        .log();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // onErrorResume: returns a fallback Publisher (Flux or Mono)
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

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

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // onErrorContinue:
  // drop the element causing the exception and continue emitting the remaining elems
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Flux<String> onErrorContinue() {
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

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // onErrorMap: map stream exceptions to business exceptions and send it to the caller.
  // It does not recover from the exception. The flow completes after this.
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Flux<String> onErrorMap() {
    return Flux.just("A", "B", "C")
        .map(name -> {
          if (name.equals("B")) {
            throw new RuntimeException("Error!");
          }
          return name;
        })
        .onErrorMap(exception -> new ReactorException(exception, exception.getMessage()))
        .log();
  }

  public Flux<String> exploreStackTrace(Exception e) {
    return Flux.just("A", "B", "C")
        .concatWith(Flux.error(e))
        .onErrorMap(ex -> {
          log.error("Exception is ", ex);
          return new ReactorException(ex, ex.getMessage());
        })
        .log();
  }

  public Flux<String> exploreStackTraceCheckpoint(Exception e) {
    return Flux.just("A", "B", "C")
        .concatWith(Flux.error(e))
        .checkpoint("errorSpot")
        .onErrorMap(ex -> {
          log.error("Exception is ", ex);
          return new ReactorException(ex, ex.getMessage());
        })
        .log();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // doOnError: catches the exception, take an action when the Exception occurs.
  // Does not modify the stream. The error is propagated to the caller.
  // Similar to try/catch block like this:
  //
  //  try {
  //    collaborator.action()
  //  } catch (Exception e) {
  //    logger.log("We've got a problem", e);
  //    throw e;
  //  }
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Flux<String> doOnError() {
    return Flux.just("A", "B", "C")
        .map(name -> {
          if (name.equals("B")) {
            throw new RuntimeException("Error!");
          }
          return name;
        })
        .doOnError(ex -> {
          log.error("We've got a problem!", ex);
        })
        .log();
  }
}
