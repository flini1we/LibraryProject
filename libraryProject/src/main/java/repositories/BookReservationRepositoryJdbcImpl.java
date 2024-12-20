package repositories;

import models.Book;
import models.BookReservation;
import models.User;
import repositories.interfaces.BookReservationRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookReservationRepositoryJdbcImpl implements BookReservationRepository {
    private final Connection connection;

    private static final String INSERT_RESERVATION = "INSERT INTO book_reservations (book_id, user_id, reservation_date, return_date, status) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_RESERVATION_BY_ID = "SELECT * FROM book_reservations WHERE id = ?";
    private static final String SELECT_ALL_RESERVATIONS = "SELECT * FROM book_reservations";
    private static final String UPDATE_RESERVATION = "UPDATE book_reservations SET book_id = ?, user_id = ?, reservation_date = ?, return_date = ?, status = ? WHERE id = ?";
    private static final String DELETE_RESERVATION = "DELETE FROM book_reservations WHERE id = ?";
    private static final String RESERVE_BOOK = "INSERT INTO book_reservations (book_id, user_id, reservation_date, return_date, status) VALUES (?, ?, ?, NULL, 'Active')";
    private static final String RETURN_BOOK = "UPDATE book_reservations SET status = 'Returned', return_date = NOW() WHERE book_id = ? AND user_id = ? AND status = 'Active'";
    private static final String UPDATE_BOOK = "UPDATE books SET available_copies = available_copies + ? WHERE id = ?";
    private static final String IS_BOOK_IS_AVAILABLE_TO_RESERVE = "SELECT available_copies FROM books WHERE id = ?";
    private static final String GET_AVAILABLE_COPIES_BY_ID = "SELECT available_copies FROM books WHERE id = ?";

    public BookReservationRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createReservation(BookReservation reservation) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_RESERVATION)) {
            stmt.setInt(1, reservation.getBook().getId());
            stmt.setInt(2, reservation.getUser().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(reservation.getReservationDate()));
            stmt.setTimestamp(4, reservation.getReturnDate() != null ? Timestamp.valueOf(reservation.getReturnDate()) : null);
            stmt.setString(5, reservation.getStatus());
            stmt.executeUpdate();
        }
    }

    @Override
    public BookReservation getReservationById(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_RESERVATION_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToReservation(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<BookReservation> getAllReservations() throws SQLException {
        List<BookReservation> reservations = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_RESERVATIONS)) {
            while (rs.next()) {
                reservations.add(mapToReservation(rs));
            }
        }
        return reservations;
    }

    @Override
    public void updateReservation(BookReservation reservation) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_RESERVATION)) {
            stmt.setInt(1, reservation.getBook().getId());
            stmt.setInt(2, reservation.getUser().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(reservation.getReservationDate()));
            stmt.setTimestamp(4, reservation.getReturnDate() != null ? Timestamp.valueOf(reservation.getReturnDate()) : null);
            stmt.setString(5, reservation.getStatus());
            stmt.setInt(6, reservation.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteReservation(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_RESERVATION)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public void reserveBook(int bookId, int userId, Timestamp reservationDate) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(RESERVE_BOOK)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, userId);
            stmt.setTimestamp(3, reservationDate);
            stmt.executeUpdate();
        }
    }

    @Override
    public void returnBook(int bookId, int userId) throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement(RETURN_BOOK)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateAvailableCopies(int bookId, int delta) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_BOOK)) {
            stmt.setInt(1, delta);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean areCopiesAvailable(int bookId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(IS_BOOK_IS_AVAILABLE_TO_RESERVE)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("available_copies") > 0;
            }
        }
        return false;
    }

    @Override
    public int getAvailableCopiesById(int bookId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(GET_AVAILABLE_COPIES_BY_ID)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("available_copies");
                }
            }
        }
        return 0;
    }

    private BookReservation mapToReservation(ResultSet resultSet) throws SQLException {
        BookReservation reservation = new BookReservation();
        reservation.setId(resultSet.getInt("id"));

        User user = new User();
        user.setId(resultSet.getInt("user_id"));
        reservation.setUser(user);

        Book book = new Book();
        book.setId(resultSet.getInt("book_id"));
        reservation.setBook(book);

        reservation.setReservationDate(resultSet.getTimestamp("reservation_date").toLocalDateTime());
        if (resultSet.getTimestamp("return_date") != null) {
            reservation.setReturnDate(resultSet.getTimestamp("return_date").toLocalDateTime());
        }
        reservation.setStatus(resultSet.getString("status"));

        return reservation;
    }
}