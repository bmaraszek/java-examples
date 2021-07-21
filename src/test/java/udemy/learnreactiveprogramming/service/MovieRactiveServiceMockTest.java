package udemy.learnreactiveprogramming.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import udemy.learnreactiveprogramming.exception.MovieException;
import udemy.learnreactiveprogramming.exception.NetworkException;
import udemy.learnreactiveprogramming.exception.ServiceException;

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
        .thenReturn(Flux.error(new RuntimeException("test exception")));

    // when
    var moviesFlux = subject.getAllMovies();

    // then
    StepVerifier.create(moviesFlux)
        .expectError(MovieException.class)
        .verify();
  }

  @Test
  @DisplayName("Should retry N times to get review info")
  void shouldRetryGettingReviewsNTimes() {
    // given
    when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
    when(reviewService.retrieveReviewsFlux(anyLong()))
        .thenThrow(
            new RuntimeException("test exception 1"),
            new RuntimeException("test exception 2"),
            new RuntimeException("test exception 3"),
            new RuntimeException("test exception 4"));

    // when
    var moviesFlux = subject.getAllMoviesRetry(3);

    // then
    StepVerifier.create(moviesFlux)
        .expectError(MovieException.class)
        .verify();

    verify(reviewService, times(4)).retrieveReviewsFlux(isA(Long.class));
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  retryWhen() tests
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  @Test
  @DisplayName("Should not retry a service exception")
  void shouldRetryGettingMovieInfoNTimesWithBackoff() {
    // given
    subject = Mockito.spy(subject);
    when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
    when(reviewService.retrieveReviewsFlux(anyLong()))
        .thenThrow(new RuntimeException("test exception 1"));

    // when
    var moviesFlux = subject.getAllMoviesRetryWhen(3);

    // then
    StepVerifier.create(moviesFlux)
        .expectError(ServiceException.class)
        .verify();

    verify(reviewService, times(1)).retrieveReviewsFlux(isA(Long.class));
  }

  @Test
  @DisplayName("Should throw ServiceException in case of network issue")
  void shouldRetryGettingMovieInfoNTimesWithBackoffAndThrowMovieException() {
    // given
    subject = Mockito.spy(subject);
    when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
    when(reviewService.retrieveReviewsFlux(anyLong()))
        .thenThrow(new NetworkException("test exception 1"));

    // when
    var moviesFlux = subject.getAllMoviesRetryWhen(3);

    // then
    StepVerifier.create(moviesFlux)
        .expectError(MovieException.class)
        .verify();

    verify(reviewService, times(4)).retrieveReviewsFlux(isA(Long.class));
  }

  @Test
  @DisplayName("Should repeat the sequence")
  void shouldRepeatTheSequence() {
    // given
    subject = Mockito.spy(subject);
    when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
    when(reviewService.retrieveReviewsFlux(anyLong())).thenCallRealMethod();

    // when
    var moviesFlux = subject.getAllMoviesRepeat(3);

    // then
    StepVerifier.create(moviesFlux)
        .expectNextCount(6)
        .thenCancel() // you can cancel a subscription from the test case itself
        .verify();

    verify(reviewService, times(6)).retrieveReviewsFlux(isA(Long.class));
  }

  @Test
  @DisplayName("Should repeat the sequence N times")
  void shouldRepeatTheSequenceNTimes() {
    // given
    subject = Mockito.spy(subject);
    when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
    when(reviewService.retrieveReviewsFlux(anyLong())).thenCallRealMethod();

    // when
    var moviesFlux = subject.getAllMoviesRepeatNTimes(3);

    // then
    StepVerifier.create(moviesFlux)
        .expectNextCount(12)  // there are 3 movies returned, and we repeat 3 MORE TIMES
        .verifyComplete();

    verify(reviewService, times(12)).retrieveReviewsFlux(isA(Long.class));
  }
}
