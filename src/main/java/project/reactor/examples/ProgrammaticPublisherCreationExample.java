package project.reactor.examples;

import reactor.core.publisher.Flux;

public class ProgrammaticPublisherCreationExample {
  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  Flux.generate()
  //    Takes an initial value and a generator function as input and continuously emits values.
  //    This is also called synchronous generate. We'll be able to generate the onNext, onComplere,
  //    onError events using SunchronousSink class. Used to emit values until a certain condition
  //    is met (smilar to a while loop).
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Flux<Integer> generate() {
    // generate a sequence from 1 to 10 and multiply each value by 2
    return Flux.generate(
        () -> 1,
        (state, sink) -> {
          sink.next(state * 2);
          if (state == 10) {
            sink.complete();
          }
          return state + 1;
        }
    );
  }
}
