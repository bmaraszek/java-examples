package udemy.learnreactiveprogramming.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    private MovieInfo movie;
    private List<Review> reviewList;
    private Revenue revenue;

    public Movie(MovieInfo movie, List<Review> reviewList) {
        this.movie = movie;
        this.reviewList = reviewList;
    }

}