package udemy.learnreactiveprogramming.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  Netty is having problems starting on Java 9+, and throws errors about not being able to access
//  the Unsafe class. In order to fix that, we need to add extra properties to the VM.
//  In IntelliJ, go to Help -> Edit Custom VM Properties, and add these lines:
//
//  --add-opens java.base/jdk.internal.misc=ALL-UNNAMED
//  -Dio.netty.tryReflectionSetAccessible=true
//
//  Netty also likes to output very wordy debug messages. Set you logger level to INFO.
//
////////////////////////////////////////////////////////////////////////////////////////////////////

public class MovieInfoServiceTest {

  WebClient webClient = WebClient.builder()
      .baseUrl("http://localhost:8080/movies")
      .build();

  MovieInfoService subject;

  @BeforeEach
  void setup() {
    subject = new MovieInfoService(webClient);
  }

  @BeforeAll
  static void setupOnce() {

  }

  @AfterAll
  static void cleanupOnce() {

  }

  @Test
  @DisplayName("Should retrieve all movie info using WebClient")
  void shouldRetrieveAllMovieInfo() {

    var movieInfoFlux = subject.retrieveMoviesFluxWebClient();

    StepVerifier.create(movieInfoFlux)
        .expectNextCount(7)
        .verifyComplete();
  }

  @Test
  @DisplayName("Should retrieve MovieInfo by Id using WebClient")
  void shouldRetrieveMovieInfoById() {
    var movieInfoMono = subject.retrieveMovieInfoMonoUsingId(1);

    StepVerifier.create(movieInfoMono)
        .expectNextMatches(m -> m.getName().equals("Batman Begins"))
        .verifyComplete();
  }
}
