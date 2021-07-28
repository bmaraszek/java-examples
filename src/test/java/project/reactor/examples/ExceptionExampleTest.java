package project.reactor.examples;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;
import reactor.tools.agent.ReactorDebugAgent;
import udemy.learnreactiveprogramming.exception.ReactorException;
import utils.StaticAppender;

public class ExceptionExampleTest {

  private ExceptionExample subject;

  @BeforeEach
  public void setup() {
    subject = new ExceptionExample();
    StaticAppender.clearEvents();
  }

  @AfterEach
  public void cleanup() {
    StaticAppender.clearEvents();
  }

  @Test
  @DisplayName("Should throw exception")
  public void shouldThrowException() {
    var flux = subject.throwException();

    StepVerifier.create(flux)
        .expectNext("A", "B", "C")
        //        .expectError(RuntimeException.class);
        .expectErrorMatches(err -> err.getMessage().contains("Exception Occurred"))
        .verify();
  }

  @Test
  @DisplayName("Should return a fallback value on error")
  public void shouldReturnFallbackValueOnError() {
    var flux = subject.onErrorReturn();

    StepVerifier.create(flux)
        .expectNext("A", "B", "C")
        .expectNext("Fallback value")
        .verifyComplete();
  }

  @Test
  @DisplayName("Should return a fallback value on error resume")
  public void shouldReturnFallbackValueOnErrorResume() {
    var flux = subject.onErrorResume(new IllegalStateException("Illegal State!"));

    StepVerifier.create(flux)
        .expectNext("A", "B", "C", "D", "E", "F")
        .verifyComplete();

    flux = subject.onErrorResume(new RuntimeException("Runtime Exception!"));

    StepVerifier.create(flux)
        .expectNext("A", "B", "C")
        .expectError(RuntimeException.class)
        .verify();
  }

  @Test
  @DisplayName("Should return a fallback value on error continue")
  public void shouldReturnFallbackValueOnErrorContinue() {
    var flux = subject.onErrorContinue();

    StepVerifier.create(flux)
        .expectNext("A", "C")
        .verifyComplete();
  }

  @Test
  @DisplayName("Should map a stream exceptions to business exception")
  public void shouldMapError() {
    var flux = subject.onErrorMap();

    StepVerifier.create(flux)
        .expectNext("A")
        .expectError(ReactorException.class)
        .verify();
  }

  @Test
  @DisplayName("Should run a task on error")
  public void shouldDoOnError() {
    var flux = subject.doOnError();

    StepVerifier.create(flux)
        .expectNext("A")
        .expectError(RuntimeException.class)
        .verify();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  A NOTE ON DEBUGGING:
  //
  //  As the code is not-blocking and a method can be executed in another thread, a stack trace may
  //  not contain useful information about which line of code caused the exception.
  //  See the trace for shouldMapError for an example of a good stack trace and exploreStackTrace()
  //  For another example.
  //
  //  Hooks.onOperatorDebug()
  //    Gives visibility on which operator caused the problem. This feature captures the stack trace
  //    for each opeartor. It needs to be activated during the start up of the application.
  //
  //  Checkpoint
  //    This will indicate an approximate location of the error by pointing to a checkpoint.
  //
  //  ReactorDebugAgent
  //    It's the recommended option for debugging exceptions in project Reactor.
  //    Java Agent will run alongside your app and collect stack trace info for each operator
  //    without any performance overhead. Need to add reactor-tools dependency to make it work.
  //    In Spring Boot, start it like this:
  //
  //  public static void main(String[] args) {
  //    ReactorDebugAgent.init();
  //    SpringApplication.run(Application.class, args);
  //  }
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  @Test
  @DisplayName("Analyzing Stack Trace")
  public void shouldThrowExceptionWithStackTrace() {
    // The Stack Trace will point to l. 113: return new ReactorException(ex, e.getMessage())
    // but not the .concatWith(Flux.error(e)) where the exception actually originated

    var flux = subject.exploreStackTrace(new IllegalStateException("My Exception"));

    StepVerifier.create(flux)
        .expectNext("A", "B", "C")
        .expectError(ReactorException.class)
        .verify();
  }

  @Test
  @DisplayName("Analyzing stack trace with Hooks.onOpeartorDebug()")
  public void shouldThrowExceptionWithStackTraceOnOpeartorDebug() {
    // In n actual app, call this from the main method to enable for the whole application
    // The stack trace will now point to the line: .concatWith(Flux.error(e))
    // This affects every operator, so it may slow down the performance of the app
    Hooks.onOperatorDebug();

    var flux = subject.exploreStackTrace(new IllegalStateException("My Exception"));

    StepVerifier.create(flux)
        .expectNext("A", "B", "C")
        .expectError(ReactorException.class)
        .verify();
  }

  @Test
  @DisplayName("Analyzing stack trace with checkpoint()")
  public void shouldThrowExceptionWithStackTraceWithCheckpoint() {
    // This will point to the checkpoint and indicate the approximate location of the problem
    var flux = subject.exploreStackTraceCheckpoint(new IllegalStateException("My Exception"));

    StepVerifier.create(flux)
        .expectNext("A", "B", "C")
        .expectError(ReactorException.class)
        .verify();
  }

  @Test
  @DisplayName("Analyzing stack trace with Reactor Debug Agent")
  public void shouldThrowExceptionWithStackTraceReactorDebugAgent() {
    ReactorDebugAgent.init();
    ReactorDebugAgent.processExistingClasses(); // need to call this when inside a test class

    var flux = subject.exploreStackTrace(new IllegalStateException("My Exception"));

    StepVerifier.create(flux)
        .expectNext("A", "B", "C")
        .expectError(ReactorException.class)
        .verify();
  }

}
