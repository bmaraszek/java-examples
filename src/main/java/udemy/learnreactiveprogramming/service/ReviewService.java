package udemy.learnreactiveprogramming.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import udemy.learnreactiveprogramming.domain.Review;

@Slf4j
public class ReviewService {

  private WebClient webClient;

  public ReviewService() {
  }

  public ReviewService(WebClient webClient) {
    this.webClient = webClient;
  }

  public Flux<Review> retrieveReviewsWebClient(long movieInfoId) {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  The endpoint to call accepts an optional query param and looks like this:
    //  /v1/reviews?movieInfoId=1
    //
    //  It returns an array, so we need to flatten it.
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////
    var uri = UriComponentsBuilder
        .fromUriString("/v1/reviews")
        .queryParam("movieInfoId", movieInfoId)
        .buildAndExpand()
        .toUriString();

    return webClient.get()
        .uri(uri)
        .retrieve()
        .bodyToFlux(Review.class)
        .log();
  }

  public List<Review> retrieveReviews(long movieInfoId) {
    log.debug("ReviewService.retrieveReviews({})", movieInfoId);
    return List.of(new Review(1L, movieInfoId, "Awesome Movie", 8.9),
        new Review(2L, movieInfoId, "Excellent Movie", 9.0));
  }

  public Flux<Review> retrieveReviewsFlux(long movieInfoId) {
    log.debug("ReviewService.retrieveReviewsFlux({})", movieInfoId);
    var reviewsList = List.of(new Review(1L, movieInfoId, "Awesome Movie", 8.9),
        new Review(2L, movieInfoId, "Excellent Movie", 9.0));
    return Flux.fromIterable(reviewsList);
  }

}
