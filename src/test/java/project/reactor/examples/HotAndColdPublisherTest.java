package project.reactor.examples;

import static udemy.learnreactiveprogramming.util.CommonUtil.delay;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

@Slf4j
public class HotAndColdPublisherTest {

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  A Cold Stream emits the elements from beginning to end for every new subscription.
  //  A Hot Stream emits only the elements that have not been emitted before.
  //
  //  Cold stream examples:
  //    - Http call with the same request
  //    - DB call with the same request
  //
  //  There are 2 types of hot streams:
  //    - Type 1 waits for the first subscription and then emits the data continuously
  //    - Type 2 emits the data continuously without the need for subscription
  //  Examples of hot streams:
  //    - Stock tickers - emit stock updates continuously as they change
  //    - Uber Driver Tracking - emit the current position of the driver continuously
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  @Test
  void coldPublisherTest() {
    var flux = Flux.range(1, 5);

    flux.subscribe(i -> log.info("Subscriber 1: {}", i));
    flux.subscribe(i -> log.info("Subscriber 2: {}", i));
  }

  @Test
  void hotPublisherTest() {
    var flux = Flux.range(1, 5)
        .delayElements(Duration.ofMillis(100));

    ConnectableFlux<Integer> connectableFlux = flux.publish();
    connectableFlux.connect();

    connectableFlux.subscribe(i -> log.info("Subscriber 1: {}", i));
    delay(200);
    connectableFlux.subscribe(i -> log.info("Subscriber 2: {}", i));
    delay(500);
  }

  @Test
  void autoConnectTest() {
    var flux = Flux.range(1, 5)
        .delayElements(Duration.ofMillis(100));

    Flux<Integer> hotSource = flux.publish().autoConnect(2);
    // autoConnect() starts emitting when the minimum number of subscribers is reached
    // refCount() is the same except it stops when the no. of subscribers drops below limit.
    // In this case, it emits cancel() signal

    hotSource.subscribe(i -> log.info("Subscriber 1: {}", i));
    delay(200);
    hotSource.subscribe(i -> log.info("Subscriber 2: {}", i));
    log.info("Two subscribers are now connected");
    delay(200);
    hotSource.subscribe(i -> log.info("Subscriber 3: {}", i));
    delay(1000);
  }

  @Test
  void refountTest() {
    var flux = Flux.range(1, 5)
        .delayElements(Duration.ofMillis(100))
        .doOnCancel(() -> log.info("Received Cancel Signal"));

    Flux<Integer> hotSource = flux.publish().refCount(2);
    // compare with autoConnect()

    var disposable1 = hotSource.subscribe(i -> log.info("Subscriber 1: {}", i));
    delay(200);
    var disposable2 = hotSource.subscribe(i -> log.info("Subscriber 2: {}", i));
    log.info("Two subscribers are now connected");
    delay(200);
    disposable1.dispose(); // dispose of subscriber 1
    disposable2.dispose();
    log.info("Subscribers 1 & 2 are now disposed");
    delay(200);
    hotSource.subscribe(i -> log.info("Subscriber 3: {}", i));
    log.info("Subscriber 3 has joined");
    delay(200);
    hotSource.subscribe(i -> log.info("Subscriber 4: {}", i));
    log.info("Subscriber 4 has joined");
    delay(1000);
  }
}
