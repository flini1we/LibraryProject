package repositories.interfaces.serviceInterfaces;

import models.dto.BookDTO;

import java.sql.SQLException;
import java.util.List;

public interface BookService {
    List<BookDTO> getAllBooks() throws SQLException;
    boolean isReservedByUser(int bookId, int userId) throws SQLException;
    boolean areCopiesAvailable(int bookId) throws SQLException;
}
