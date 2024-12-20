package repositories.interfaces;

import models.Review;

import java.sql.SQLException;
import java.util.List;

public interface ReviewRepository {
    void createReview(Review review) throws SQLException;

    List<Review> getReviewsByBookId(int bookId) throws SQLException;

    Review getReviewById(int id) throws SQLException;

    List<Review> getAllReviews() throws SQLException;

    void updateReview(Review review) throws SQLException;

    void deleteReview(int id) throws SQLException;
}
