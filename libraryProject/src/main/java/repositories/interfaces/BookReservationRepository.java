package repositories.interfaces;

import models.BookReservation;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface BookReservationRepository {
    void createReservation(BookReservation reservation) throws SQLException;

    BookReservation getReservationById(int id) throws SQLException;

    List<BookReservation> getAllReservations() throws SQLException;

    void updateReservation(BookReservation reservation) throws SQLException;

    void deleteReservation(int id) throws SQLException;

    void reserveBook(int bookId, int userId, Timestamp reservationDate) throws Exception;

    void returnBook(int bookId, int userId) throws Exception;

    void updateAvailableCopies(int bookId, int delta) throws Exception;

    boolean areCopiesAvailable(int bookId) throws Exception;

    int getAvailableCopiesById(int bookId) throws SQLException;
}
