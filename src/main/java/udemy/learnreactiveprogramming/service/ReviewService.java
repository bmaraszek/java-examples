package udemy.learnreactiveprogramming.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import udemy.learnreactiveprogramming.domain.Review;

@Slf4j
public class ReviewService {

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
