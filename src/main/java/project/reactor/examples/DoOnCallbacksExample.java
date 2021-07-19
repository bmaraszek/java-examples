package project.reactor.examples;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class DoOnCallbacksExample {

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  DoOn CallBck operators are used for side effects and they do not modify the stream.
  //  There are different operators available:
  //    - doOnSubscribe()
  //    - doOnNext()
  //    - doOnComplate()
  //    - doOnError()
  //    - doFinally()
  //
  //  Example use cases:
  //    - Debugging an issue in your local env
  //    - Send a notification when the reactive sequence complates or errors out
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Flux<String> doOnNextExample(int stringLength) {
    return Flux.fromIterable(List.of("luke", "leia", "han"))
        .filter(s -> s.length() > stringLength)
        .map(String::toUpperCase)
        .doOnNext(name -> {
          log.debug("doOnNext(): {}", name);
        })
        .doOnSubscribe(subscription -> {
          log.debug("Subscription is: {}", subscription);
        })
        .doOnComplete(() -> {
          log.debug("onComplete()");
        })
        .doFinally(signal -> {
          log.debug("onFinally(): singalType is: {}", signal.name());
        })
        .log();
  }
}
