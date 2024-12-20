package repositories;

import lombok.SneakyThrows;
import models.dto.BookDTO;
import repositories.interfaces.BookReservationRepository;
import repositories.interfaces.ReserveRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReserveRepositoryJdbcImpl implements ReserveRepository {

    private final Connection connection;
    private final BookReservationRepository bookReservationRepository;

    private static final String DO_USER_HAVE_RESERVED_BOOKS = "SELECT COUNT(*) FROM book_reservations WHERE user_id = ? AND status = 'Active'";
    private static final String IS_BOOK_RESERVED_BY_USER = "SELECT * FROM book_reservations WHERE book_id = ? AND user_id = ? AND status = 'Active'";
    private static final String GET_ALL_BOOKS = "SELECT b.id, b.title, a.name AS author, b.publication_year, b.description, i.image_path, b.isbn, b.available_copies, b.total_copies FROM books b JOIN book_authors ba ON b.id = ba.book_id  JOIN authors a ON ba.author_id = a.id  LEFT JOIN images i ON b.id = i.book_id";
    private static final String  GET_ALL_RESERVED_BY_CURRENT_USER = "SELECT b.id, b.title, a.name AS author, b.publication_year, b.description, i.image_path, b.isbn, b.available_copies, b.total_copies, br.status FROM book_reservations br JOIN books b ON br.book_id = b.id JOIN authors a ON b.author_id = a.id LEFT JOIN images i ON b.id = i.book_id WHERE br.user_id = ? AND br.status = 'Active'";

    public ReserveRepositoryJdbcImpl(Connection connection) throws SQLException {
        this.connection = connection;
        this.bookReservationRepository = new BookReservationRepositoryJdbcImpl(connection);
    }

    @Override
    public boolean doUserHaveReservedBooks(int userId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DO_USER_HAVE_RESERVED_BOOKS)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            return false;
        }
    }

    @SneakyThrows
    @Override
    public boolean isBookReservedByUser(int bookId, int userId) {
        try (PreparedStatement stmt = connection.prepareStatement(IS_BOOK_RESERVED_BY_USER)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<BookDTO> getAllBooksWithReserveInfo(int userId) throws SQLException {
        List<BookDTO> books = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(GET_ALL_BOOKS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BookDTO bookDTO = new BookDTO();
                bookDTO.setId(rs.getInt("id"));
                bookDTO.setTitle(rs.getString("title"));
                bookDTO.setAuthorName(rs.getString("author"));
                bookDTO.setPublicationYear(rs.getInt("publication_year"));
                bookDTO.setDescription(rs.getString("description"));
                bookDTO.setIsbn(rs.getString("isbn"));
                bookDTO.setAvailableCopies(rs.getInt("available_copies"));
                bookDTO.setTotalCopies(rs.getInt("total_copies"));
                bookDTO.setImagePath(rs.getString("image_path"));

                boolean isReservedByUser = isBookReservedByUser(bookDTO.getId(), userId);
                bookDTO.setReservedByUser(isReservedByUser);

                boolean isAvailable = false;
                try {
                    isAvailable = bookReservationRepository.areCopiesAvailable(bookDTO.getId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                bookDTO.setAvailableCopiesPresent(isAvailable);

                books.add(bookDTO);
            }
        }

        return books;
    }

    @Override
    public List<BookDTO> getReservedBooksByUser(int userId) throws SQLException {
        List<BookDTO> reservedBooks = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(GET_ALL_RESERVED_BY_CURRENT_USER)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BookDTO bookDTO = new BookDTO();
                    bookDTO.setId(rs.getInt("id"));
                    bookDTO.setTitle(rs.getString("title"));
                    bookDTO.setAuthorName(rs.getString("author"));
                    bookDTO.setPublicationYear(rs.getInt("publication_year"));
                    bookDTO.setDescription(rs.getString("description"));
                    bookDTO.setIsbn(rs.getString("isbn"));
                    bookDTO.setAvailableCopies(rs.getInt("available_copies"));
                    bookDTO.setTotalCopies(rs.getInt("total_copies"));
                    bookDTO.setImagePath(rs.getString("image_path"));

                    boolean isReservedByUser = isBookReservedByUser(bookDTO.getId(), userId);
                    bookDTO.setReservedByUser(isReservedByUser);

                    boolean isAvailable = false;
                    try {
                        isAvailable = bookReservationRepository.areCopiesAvailable(bookDTO.getId());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    bookDTO.setAvailableCopiesPresent(isAvailable);

                    reservedBooks.add(bookDTO);
                }
            }
        }

        return reservedBooks;
    }
}
