package project.reactor.examples;

import static udemy.learnreactiveprogramming.util.CommonUtil.delay;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

public class ProgrammaticPublisherCreationExample {

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  Sinks are constructs through which Reactive Streams singnals can be programmatically pushed,
  //  with Flux or Mono semantics. The Sink class exposes a collection of Sinks.Many and Sinks.One
  //  factories.
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

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

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  Flux.create()
  //    Used to bridge an existing API to the Reactive World. This is Async and Multithreaded.
  //    We can generate/emit these events from multiple threads. We'll be able to generate the
  //    onNext, onComplete, and onError events using FluxSing class. Multiple emissions in a single 
  //    round is supported.
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Flux<String> create() {
    return Flux.create(sink -> {
      getNamesWithDelay().forEach(sink::next);
      // the same event can be emitted twice by calling sink.next(); sink.next()
      // this would not work for Flux.generate()
      // this is the multiple emission
      sink.complete();
    });
  }

  public Flux<String> createAsync() {
    return Flux.create(sink -> CompletableFuture
        .supplyAsync(ProgrammaticPublisherCreationExample::getNamesWithDelay)
        .thenAccept(names -> names.forEach(sink::next))
        .thenRun(sink::complete));
  }

  public Flux<String> createAsyncMultithreaded() {
    return Flux.create(sink -> CompletableFuture
        .supplyAsync(ProgrammaticPublisherCreationExample::getNamesWithDelay)
        .thenAccept(names -> names.forEach(sink::next))
        .thenRun(() -> sendEvents(sink)));
  }

  public void sendEvents(FluxSink<String> sink) {
    CompletableFuture
        .supplyAsync(ProgrammaticPublisherCreationExample::getNamesWithDelay)
        .thenAccept(names -> names.forEach(sink::next))
        .thenRun(sink::complete);
  }

  public static List<String> getNamesWithDelay() {
    delay(1000);
    return List.of("Luke", "Leia", "Han");
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  Mono only has the create() method, and it can only emit one event
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Mono<String> createMono() {
    return Mono.create(sink -> sink.success("Luke"));
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  Handle - this operator is pretty close to generate.
  //  Functionally it's like a combination of map and filter.
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Flux<String> handle() {
    return Flux.fromIterable(List.of("Frodo", "Sam", "Merry", "Pippin"))
        .handle((name, sink) -> {
          if (name.length() > 3) {
            sink.next(name.toUpperCase());
          }
        });
  }
}
