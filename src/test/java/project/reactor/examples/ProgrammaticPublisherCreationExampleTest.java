package project.reactor.examples;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class ProgrammaticPublisherCreationExampleTest {

  ProgrammaticPublisherCreationExample subject;

  @BeforeEach
  void setUp() {
    subject = new ProgrammaticPublisherCreationExample();
  }

  @Test
  void shouldGenerateValues() {
    var flux = subject.generate().log();

    StepVerifier.create(flux)
        .expectNext(2, 4, 6, 8, 10, 12, 14, 16, 18, 20)
        .verifyComplete();
  }

}