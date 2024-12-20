package service;

import lombok.AllArgsConstructor;
import models.Review;
import repositories.ReviewRepositoryJdbcImpl;

import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
public class ReviewServiceImpl implements repositories.interfaces.serviceInterfaces.ReviewService {
    private final ReviewRepositoryJdbcImpl reviewRepository;

    @Override
    public void createReview(Review review) throws SQLException {
        reviewRepository.createReview(review);
    }

    @Override
    public Review getReviewById(int id) throws SQLException {
        return reviewRepository.getReviewById(id);
    }

    @Override
    public List<Review> getAllReviews() throws SQLException {
        return reviewRepository.getAllReviews();
    }

    @Override
    public List<Review> getReviewsByBookId(int bookId) throws SQLException {
        return reviewRepository.getReviewsByBookId(bookId);
    }

    @Override
    public void updateReview(Review review) throws SQLException {
        reviewRepository.updateReview(review);
    }

    @Override
    public void deleteReview(int id) throws SQLException {
        reviewRepository.deleteReview(id);
    }
}
