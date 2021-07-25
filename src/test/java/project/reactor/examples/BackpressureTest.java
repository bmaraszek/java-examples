package project.reactor.examples;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

@Slf4j
public class BackpressureTest {

  @Test
  void testNoBackPressure() {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  onSubscribe([Synchronous Fuseable] FluxRange.RangeSubscription)
    //  request(unbounded)
    //  onNext(1)
    //  Number is: 1
    //  onNext(2)
    //  Number is: 2
    //  ...
    //  Number is: 100
    //  onComplete()
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////
    Flux<Integer> numberRange = Flux.range(1, 100).log();

    numberRange.subscribe(num -> {
      log.info("Number is: {}", num);
    });
  }

  @Test
  void testBackPressure() {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // BaseSubscriber is a class used to control the subscription in more detail.
    // Override the methods you need.
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////
    Flux<Integer> numberRange = Flux.range(1, 100).log();

    numberRange.subscribe(new BaseSubscriber<>() {
      @Override
      protected void hookOnSubscribe(Subscription subscription) {
        request(2);
      }

      @Override
      protected void hookOnNext(Integer value) {
        log.info("The value is: {}", value);
        if (value == 2) {
          cancel();
        }
      }

      @Override
      protected void hookOnComplete() {

      }

      @Override
      protected void hookOnError(Throwable throwable) {
//        super.hookOnError(throwable);
      }

      @Override
      protected void hookOnCancel() {
        log.info("inside onCancel");
      }
    });
  }

  @Test
  void shouldRequestTwoElemsAtATime() throws InterruptedException {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  The issue here is that we're continuously sending request(2) to the Publisher.
    //  If that publisher were a remote DB, sending this many requests might be problematic.
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////
    Flux<Integer> numberRange = Flux.range(1, 100).log();
    CountDownLatch latch = new CountDownLatch(1);

    numberRange.subscribe(new BaseSubscriber<>() {
      @Override
      protected void hookOnSubscribe(Subscription subscription) {
        request(2);
      }

      @Override
      protected void hookOnNext(Integer value) {
        log.info("The value is: {}", value);
        if (value % 2 == 0 || value < 50) {
          request(2);
        } else {
          cancel();
        }
      }

      @Override
      protected void hookOnCancel() {
        log.info("inside onCancel");
        latch.countDown();
      }
    });

    Assertions.assertTrue(latch.await(5L, TimeUnit.SECONDS));
  }
}
