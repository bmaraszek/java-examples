package udemy.learnreactiveprogramming.service;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import udemy.learnreactiveprogramming.domain.Movie;
import udemy.learnreactiveprogramming.domain.Review;
import udemy.learnreactiveprogramming.exception.MovieException;

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

}
