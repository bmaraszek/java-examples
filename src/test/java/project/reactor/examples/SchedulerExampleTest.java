package project.reactor.examples;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class SchedulerExampleTest {

  private SchedulersExample subject = new SchedulersExample();

  @Test
  public void testExplorePublishOn() {
    Flux<String> flux = subject.explorePublishOn();

    StepVerifier.create(flux)
        .expectNextCount(6)
        .verifyComplete();
  }

  @Test
  public void testExploreSubscribeOn() {
    Flux<String> flux = subject.exploreSubscribeOn();

    StepVerifier.create(flux)
        .expectNextCount(6)
        .verifyComplete();
  }
}
