package udemy.learnreactiveprogramming.service;

import java.util.List;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import udemy.learnreactiveprogramming.domain.Movie;
import udemy.learnreactiveprogramming.domain.Review;

@AllArgsConstructor
public class MovieReactiveService {

  private MovieInfoService movieInfoService;
  private ReviewService reviewService;

  public Flux<Movie> getAllMovies() {
    var moviesInfoFlux = movieInfoService.retrieveMoviesFlux();
    Flux<Movie> movieFlux = moviesInfoFlux
        // when we have a Rx type and want to return antoher Rx type, use flatMap()
        .flatMap(movieInfo -> {
          Mono<List<Review>> reviewsMono = reviewService
              .retrieveReviewsFlux(movieInfo.getMovieInfoId())
              .collectList();
          Mono<Movie> movieMono = reviewsMono
              .map(reviewList -> new Movie(movieInfo, reviewList));

          return movieMono;
        });
    return movieFlux;
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

}
