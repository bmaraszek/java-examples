package udemy.learnreactiveprogramming.service;

import java.time.Duration;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;
import udemy.learnreactiveprogramming.domain.Movie;
import udemy.learnreactiveprogramming.domain.Review;
import udemy.learnreactiveprogramming.exception.MovieException;
import udemy.learnreactiveprogramming.exception.NetworkException;
import udemy.learnreactiveprogramming.exception.ServiceException;

@AllArgsConstructor
@Slf4j
public class MovieReactiveService {

  private MovieInfoService movieInfoService;
  private ReviewService reviewService;

  public Flux<Movie> getAllMovies() {
    return movieInfoService.retrieveMoviesFlux()
        // when we have a Rx type and want to return antoher Rx type, use flatMap()
        .flatMap(movieInfo -> {
          Mono<List<Review>> reviewsMono = reviewService
              .retrieveReviewsFlux(movieInfo.getMovieInfoId())
              .collectList();
          Mono<Movie> movieMono = reviewsMono
              .map(reviewList -> new Movie(movieInfo, reviewList));

          return movieMono;
        })
        .onErrorMap((ex) -> {
          log.error("Error while retrieving a movie", ex);
          return new MovieException(ex);
        });
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  If we used map() instaed of flatMap(), our result would be of tyoe Flux<Mono<Movie>>
  //  instaed of Flux<Movie>.
  //
  //  That's beause the map transofrms movieInfo into a *MONO* of Movies.
  //  We want to do that, but then flatten the Monos into the original Flux.
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Flux<Mono<Movie>> getAllMovies2() {
    var moviesInfoFlux = movieInfoService.retrieveMoviesFlux();
    Flux<Mono<Movie>> result = moviesInfoFlux
        .map(movieInfo -> {
          Mono<List<Review>> reviewsMono = reviewService
              .retrieveReviewsFlux(movieInfo.getMovieInfoId())
              .collectList();

          Mono<Movie> movieMono = reviewsMono
              .map(reviewList -> new Movie(movieInfo, reviewList));

          return movieMono;
        });
    return result;
  }

  public Mono<Movie> getMovieById(long movieId) {
    var movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
    var reviewFlux = reviewService
        .retrieveReviewsFlux(movieId)
        .collectList();

    return movieInfoMono
        .zipWith(reviewFlux, (movieInfo, reviews) -> new Movie(movieInfo, reviews));
  }

  public Mono<Movie> getMovieByIdFlatMap(long movieId) {
    var movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);

    return movieInfoMono.flatMap(movieInfo -> {
      var reviewsMono = reviewService
          .retrieveReviewsFlux(movieId)
          .collectList();

      return reviewsMono.map(
          reviewList -> new Movie(movieInfo, reviewList));
    });
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  Retry functionality is used primarily when calling services that can fail.
  //    - retry() - will retry the failed exception indefinitely - not often used
  //    - retry(long n) - retry n number of times
  //
  //  Caution: in the example below, we will not be able to retry retrieveMoviesFlux(),
  //           as retry() re-subscribes to the Flux sequence. If the Flux sequence
  //           is not crated properly, it cannot be resubscribed to.
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Flux<Movie> getAllMoviesRetry(int maxRetries) {
    return movieInfoService.retrieveMoviesFlux()
        // when we have a Rx type and want to return antoher Rx type, use flatMap()
        .flatMap(movieInfo -> {
          Mono<List<Review>> reviewsMono = reviewService
              .retrieveReviewsFlux(movieInfo.getMovieInfoId())
              .collectList();
          Mono<Movie> movieMono = reviewsMono
              .map(reviewList -> new Movie(movieInfo, reviewList));

          return movieMono;
        })
        .onErrorMap((ex) -> {
          log.error("Error while retrieving a movie {}", ex.getMessage());
          return new MovieException(ex);
        })
        .retry(maxRetries)
        .log();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  retryWhen() allows for more customization
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Flux<Movie> getAllMoviesRetryWhen(int maxRetries) {
    return movieInfoService.retrieveMoviesFlux()
        // when we have a Rx type and want to return antoher Rx type, use flatMap()
        .flatMap(movieInfo -> {
          Mono<List<Review>> reviewsMono = reviewService
              .retrieveReviewsFlux(movieInfo.getMovieInfoId())
              .collectList();
          Mono<Movie> movieMono = reviewsMono
              .map(reviewList -> new Movie(movieInfo, reviewList));

          return movieMono;
        })
        .onErrorMap((ex) -> {
          log.error("Error while retrieving a movie {}", ex.getMessage());
          if (ex instanceof NetworkException) {
            return new MovieException(ex.getMessage());
          } else {
            return new ServiceException(ex.getMessage());
          }
        })
        .retryWhen(getBackoff(maxRetries))
        .log();
  }

  private RetryBackoffSpec getBackoff(int maxRetries) {
    return Retry.backoff(maxRetries, Duration.ofMillis(1000))
        .filter(ex -> ex instanceof MovieException)
        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
            Exceptions.propagate(retrySignal.failure())
        );
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  //  repeat() is used to repeat an existing sequence.
  //  This operator is invoked after the onCompletion() event from the existing sequence.
  //  Use it when you have an use-case to subscribe to the same publisher again.
  //  This opeartor works as long as NO EXCEPTION is thrown.
  //
  //  repeat(long n) will repeat n times.
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Flux<Movie> getAllMoviesRepeat(int maxRetries) {
    return movieInfoService.retrieveMoviesFlux()
        // when we have a Rx type and want to return antoher Rx type, use flatMap()
        .flatMap(movieInfo -> {
          Mono<List<Review>> reviewsMono = reviewService
              .retrieveReviewsFlux(movieInfo.getMovieInfoId())
              .collectList();
          Mono<Movie> movieMono = reviewsMono
              .map(reviewList -> new Movie(movieInfo, reviewList));

          return movieMono;
        })
        .onErrorMap((ex) -> {
          log.error("Error while retrieving a movie {}", ex.getMessage());
          if (ex instanceof NetworkException) {
            return new MovieException(ex.getMessage());
          } else {
            return new ServiceException(ex.getMessage());
          }
        })
        .retryWhen(getBackoff(maxRetries))
        .repeat() // repeats indifinitely
        .log();
  }

  public Flux<Movie> getAllMoviesRepeatNTimes(int repeatCount) {
    return movieInfoService.retrieveMoviesFlux()
        // when we have a Rx type and want to return antoher Rx type, use flatMap()
        .flatMap(movieInfo -> {
          Mono<List<Review>> reviewsMono = reviewService
              .retrieveReviewsFlux(movieInfo.getMovieInfoId())
              .collectList();
          Mono<Movie> movieMono = reviewsMono
              .map(reviewList -> new Movie(movieInfo, reviewList));

          return movieMono;
        })
        .onErrorMap((ex) -> {
          log.error("Error while retrieving a movie {}", ex.getMessage());
          if (ex instanceof NetworkException) {
            return new MovieException(ex.getMessage());
          } else {
            return new ServiceException(ex.getMessage());
          }
        })
        .retryWhen(getBackoff(3))
        .repeat(repeatCount) // repeats indifinitely
        .log();
  }

}
