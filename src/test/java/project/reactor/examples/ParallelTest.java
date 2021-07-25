package project.reactor.examples;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

@Slf4j
public class ParallelTest {

  ParallelExamples subject;

  @BeforeEach
  void setup() {
    subject = new ParallelExamples();
  }

  @Test
  void parallelFluxTest() {
    log.info("This machine has {} cores", Runtime.getRuntime().availableProcessors());
    var names = subject.parallel();

    StepVerifier.create(names)
//        .expectNext("LUKE", "LEIA", "HAN", "CHEWBACCA") // the order will not be preserved
        .expectNextCount(4)
        .verifyComplete();
  }

  @Test
  void parallelFlatMapTest() {
    log.info("This machine has {} cores", Runtime.getRuntime().availableProcessors());
    var names = subject.parallelFlatMap();

    StepVerifier.create(names)
        .expectNextCount(4)
        .verifyComplete();
  }

  @Test
  void parallelFlatMapWithMergeTest() {
    log.info("This machine has {} cores", Runtime.getRuntime().availableProcessors());
    var names = subject.parallelFlatMapWithMerge();

    StepVerifier.create(names)
        .expectNextCount(8)
        .verifyComplete();
  }

  @Test
  void parallelFlatMapSequentialTest() {
    log.info("This machine has {} cores", Runtime.getRuntime().availableProcessors());
    var names = subject.parallelFlatMapSequential();

    StepVerifier.create(names)
        .expectNext("LUKE", "LEIA", "HAN", "CHEWBACCA")
        .verifyComplete();
  }

}
