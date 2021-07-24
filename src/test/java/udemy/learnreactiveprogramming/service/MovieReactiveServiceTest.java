package udemy.learnreactiveprogramming.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import udemy.learnreactiveprogramming.domain.Movie;

@Slf4j
class MovieReactiveServiceTest {

  private MovieReactiveService subject;

  @BeforeEach
  void setUp() {
    subject = new MovieReactiveService(
        new MovieInfoService(), new ReviewService(), new RevenueService()
    );
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void getAllMovies() {
    Flux<Movie> allMovies = subject.getAllMovies();

    allMovies.subscribe(movie -> log.info(movie.toString()));

    StepVerifier.create(allMovies)
        .assertNext(movie -> {
          assertEquals("Batman Begins", movie.getMovie().getName());
          assertEquals(2, movie.getReviewList().size());
        })
        .assertNext(movie -> {
          assertEquals("The Dark Knight", movie.getMovie().getName());
          assertEquals(2, movie.getReviewList().size());
        })
        .assertNext(movie -> {
          assertEquals("Dark Knight Rises", movie.getMovie().getName());
          assertEquals(2, movie.getReviewList().size());
        })
        .verifyComplete();
  }

  @Test
  void getMovieById() {
    var movieById = subject.getMovieById(123L).log();

    StepVerifier.create(movieById)
        .assertNext(movie -> {
          assertEquals("Batman Begins", movie.getMovie().getName());
          assertEquals(2, movie.getReviewList().size());
        })
        .verifyComplete();
  }

  @Test
  void getMovieByIdFlatMap() {
    var movieById = subject.getMovieByIdFlatMap(123L).log();

    StepVerifier.create(movieById)
        .assertNext(movie -> {
          assertEquals("Batman Begins", movie.getMovie().getName());
          assertEquals(2, movie.getReviewList().size());
        })
        .verifyComplete();
  }

}