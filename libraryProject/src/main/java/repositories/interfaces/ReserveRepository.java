package repositories.interfaces;

import models.dto.BookDTO;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface ReserveRepository {
    boolean doUserHaveReservedBooks(int userId) throws SQLException;

    boolean isBookReservedByUser(int bookId, int userId);

    List<BookDTO> getAllBooksWithReserveInfo(int userId) throws SQLException;

    List<BookDTO> getReservedBooksByUser(int userId) throws SQLException;
}
