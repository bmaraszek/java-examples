package udemy.learnreactiveprogramming.service;

import java.util.List;
import reactor.core.publisher.Flux;
import udemy.learnreactiveprogramming.domain.Review;

public class ReviewService {

  public List<Review> retrieveReviews(long movieInfoId) {

    return List.of(new Review(1L, movieInfoId, "Awesome Movie", 8.9),
        new Review(2L, movieInfoId, "Excellent Movie", 9.0));
  }

  public Flux<Review> retrieveReviewsFlux(long movieInfoId) {

    var reviewsList = List.of(new Review(1L, movieInfoId, "Awesome Movie", 8.9),
        new Review(2L, movieInfoId, "Excellent Movie", 9.0));
    return Flux.fromIterable(reviewsList);
  }

}
