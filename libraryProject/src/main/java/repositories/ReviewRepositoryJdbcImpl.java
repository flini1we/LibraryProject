package repositories;

import models.Review;
import repositories.interfaces.ReviewRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewRepositoryJdbcImpl implements ReviewRepository {
    private final Connection connection;

    private static final String INSERT_REVIEW = "INSERT INTO reviews (book_id, user_id, rating, comment, review_date) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_REVIEW_BY_ID =
            "SELECT r.id, r.book_id, b.title as book_title, r.user_id, u.username, r.rating, r.review_text, r.review_date " +
                    "FROM reviews r " +
                    "JOIN users u ON r.user_id = u.id " +
                    "JOIN books b ON r.book_id = b.id " +
                    "WHERE r.id = ?";
    private static final String SELECT_ALL_REVIEWS =
            "SELECT r.id, r.book_id, b.title as book_title, r.user_id, u.username, r.rating, r.review_text, r.review_date " +
                    "FROM reviews r " +
                    "JOIN users u ON r.user_id = u.id " +
                    "JOIN books b ON r.book_id = b.id";
    private static final String SELECT_REVIEWS_BY_BOOK_ID =
            "SELECT r.id, r.book_id, b.title as book_title, r.user_id, u.username, r.rating, r.review_text, r.review_date " +
                    "FROM reviews r " +
                    "JOIN users u ON r.user_id = u.id " +
                    "JOIN books b ON r.book_id = b.id " +
                    "WHERE r.book_id = ?";
    private static final String UPDATE_REVIEW = "UPDATE reviews SET book_id = ?, user_id = ?, rating = ?, review_text = ?, review_date = ? WHERE id = ?";
    private static final String DELETE_REVIEW = "DELETE FROM reviews WHERE id = ?";

    public ReviewRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createReview(Review review) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_REVIEW)) {
            stmt.setInt(1, review.getBookId());
            stmt.setInt(2, review.getUserId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getReviewText());
            stmt.setTimestamp(5, review.getReviewDate());
            stmt.executeUpdate();
        }
    }

    @Override
    public Review getReviewById(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_REVIEW_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToReview(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Review> getAllReviews() throws SQLException {
        List<Review> reviews = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_ALL_REVIEWS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                reviews.add(mapToReview(rs));
            }
        }
        return reviews;
    }

    @Override
    public List<Review> getReviewsByBookId(int bookId) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_REVIEWS_BY_BOOK_ID)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapToReview(rs));
                }
            }
        }
        return reviews;
    }

    @Override
    public void updateReview(Review review) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_REVIEW)) {
            stmt.setInt(1, review.getBookId());
            stmt.setInt(2, review.getUserId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getReviewText());
            stmt.setTimestamp(5, review.getReviewDate());
            stmt.setInt(6, review.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteReview(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_REVIEW)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Review mapToReview(ResultSet rs) throws SQLException {
        return new Review(
                rs.getInt("id"),
                rs.getInt("book_id"),
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getString("book_title"),
                rs.getInt("rating"),
                rs.getString("review_text"),
                rs.getTimestamp("review_date")
        );
    }
}