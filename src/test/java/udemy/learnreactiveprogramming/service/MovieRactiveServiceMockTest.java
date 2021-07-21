package udemy.learnreactiveprogramming.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import udemy.learnreactiveprogramming.exception.MovieException;

@Slf4j
@ExtendWith(MockitoExtension.class)
class MovieRactiveServiceMockTest {

  @InjectMocks
  private MovieReactiveService subject;

  @Mock
  private MovieInfoService movieInfoService;

  @Mock
  private ReviewService reviewService;

  @Test
  @DisplayName("Should return movies when collaborators return results")
  void shouldThrowMovieException() {
    // given
    when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
    when(reviewService.retrieveReviewsFlux(anyLong())).thenCallRealMethod();

    // when
    var moviesFlux = subject.getAllMovies();

    // then
    StepVerifier.create(moviesFlux)
        .expectNextCount(3)
        .verifyComplete();
  }

  @Test
  @DisplayName("Should throw a MovieException when unable to get reviews")
  void shouldThrowMovieExceptionWhenCannotGetReviews() {
    // given
    when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
    when(reviewService.retrieveReviewsFlux(anyLong()))
        .thenThrow(new RuntimeException("test exception"));

    // when
    var moviesFlux = subject.getAllMovies();

    // then
    StepVerifier.create(moviesFlux)
        .expectError(MovieException.class)
        .verify();
  }

  @Test
  @DisplayName("Should throw a MovieException when unable to get movie info")
  void shouldThrowMovieExceptionWhenCannotGetMovieInfo() {
    // given
    when(movieInfoService.retrieveMoviesFlux())
        .thenReturn(Flux.error(new RuntimeException("test excpetion")));

    // when
    var moviesFlux = subject.getAllMovies();

    // then
    StepVerifier.create(moviesFlux)
        .expectError(MovieException.class)
        .verify();
  }

}
