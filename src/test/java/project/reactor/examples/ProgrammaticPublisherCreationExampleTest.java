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

  @Test
  void shouldCreateValues() {
    var flux = subject.create().log();

    StepVerifier.create(flux)
        .expectNext("Luke", "Leia", "Han")
        .verifyComplete();
  }

  @Test
  void shouldCreateAsync() {
    var flux = subject.createAsync().log();

    StepVerifier.create(flux)
        .expectNext("Luke", "Leia", "Han")
        .verifyComplete();
  }

  @Test
  void shouldCreateAsyncMultithreaded() {
    var flux = subject.createAsyncMultithreaded().log();

    StepVerifier.create(flux)
        .expectNext("Luke", "Leia", "Han", "Luke", "Leia", "Han")
        .verifyComplete();
  }

  @Test
  void shouldCreateAMono() {
    var mono = subject.createMono();

    StepVerifier.create(mono)
        .expectNext("Luke")
        .verifyComplete();
  }

  @Test
  void shouldHandle() {
    var flux = subject.handle();

    StepVerifier.create(flux)
        .expectNext("FRODO", "MERRY", "PIPPIN")
        .verifyComplete();
  }

}