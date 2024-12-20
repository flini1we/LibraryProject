package repositories.interfaces;

import models.Book;
import models.Review;
import models.dto.BookDTO;

import java.sql.SQLException;
import java.util.List;

public interface BookRepository {
    void createBook(Book book) throws SQLException;

    Book getBookById(int id) throws SQLException;

    BookDTO findBookDTOById(int bookId) throws SQLException;

    List<BookDTO> getAllBooks() throws SQLException;

    void updateBook(Book book) throws SQLException;

    void deleteBook(int id) throws SQLException;

    boolean isReservedByUser(int bookId, int userId) throws SQLException;

    List<Review> getReviewsForBook(int bookId) throws SQLException;

    boolean increaseBookCopies(int bookId) throws SQLException;

    boolean decreaseBookCopies(int bookId) throws SQLException;
}
