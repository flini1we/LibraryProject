package models;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Review {
    private int id;
    private int bookId;
    private int userId;
    private String username;
    private String bookTitle;
    private int rating;
    private String reviewText;
    private Timestamp reviewDate;


    public Review(int bookId, int userId, int rating, String reviewText, Timestamp reviewDate) {
        this.bookId = bookId;
        this.userId = userId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
    }
}
