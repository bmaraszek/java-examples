package udemy.learnreactiveprogramming.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class ReviewServiceTest {
  WebClient webClient = WebClient.builder()
      .baseUrl("http://localhost:8080/movies")
      .build();
  ReviewService subject;

  @BeforeEach
  void setup() {
    subject = new ReviewService(webClient);
  }

  @BeforeAll
  static void setupOnce() {

  }

  @AfterAll
  static void cleanupOnce() {

  }

  @Test
  @DisplayName("Should retrieve a review based on movie info id")
  void shouldRetrieveReviewById() {

    var movieInfoFlux = subject.retrieveReviewsWebClient(1L);

    StepVerifier.create(movieInfoFlux)
        .expectNextMatches(review ->
            review.getMovieInfoId().equals(1L) && review.getRating().equals(8.2))
        .verifyComplete();
  }
}
