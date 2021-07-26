package udemy.learnreactiveprogramming.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

@Slf4j
class MovieReactiveServiceWebClientTest {

  WebClient webClient = WebClient.builder()
      .baseUrl("http://localhost:8080/movies")
      .build();

  private MovieInfoService movieInfoService;
  private ReviewService reviewService;
  private RevenueService revenueService;

  private MovieReactiveService subject;

  @BeforeEach
  void setUp() {
    movieInfoService = new MovieInfoService(webClient);
    reviewService = new ReviewService(webClient);
    revenueService = new RevenueService();

    subject = new MovieReactiveService(movieInfoService, reviewService, revenueService);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  @DisplayName("Should get all movies using Web Client")
  void getAllMoviesWebClientTest() {
    var allMovies = subject.getAllMoviesWebClient();

    StepVerifier.create(allMovies)
        .expectNextCount(7)
        .verifyComplete();
  }

  @Test
  @DisplayName("Should get movie by Id using Web Client")
  void getMovieByIdWebClientTest() {
    var movie = subject.getMovieByIdWebClient(1L);

    StepVerifier.create(movie)
        .expectNextMatches(
            m -> m.getMovie().getName().equals("Batman Begins")
                && m.getReviewList().size() > 0)
        .verifyComplete();
  }
}