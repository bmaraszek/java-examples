package project.reactor.examples;

import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

public class VirtualTimeSchedulerTest {

  private VirtualTimeSchedulerExample subject;

  @BeforeEach
  void setup() {
    subject = new VirtualTimeSchedulerExample();
  }

  @Test
  void virtualTimeSchedulerTest() {
    VirtualTimeScheduler.getOrSet();

    var flux = subject.concatMap(Duration.ofSeconds(1)); // should take 12 [s]

    StepVerifier.withVirtualTime(() -> flux)
        .thenAwait(Duration.ofSeconds(15))  // let's pretend 15 [s] have passed
        .expectNext("L", "U", "K", "E", "L", "E", "I", "A", "H", "A", "N")
        .verifyComplete();

    // This will work with Flux.delayElements(Duration delay) etc. but not with Thread.sleep()
  }
}
