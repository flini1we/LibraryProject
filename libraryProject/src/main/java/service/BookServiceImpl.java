package service;

import lombok.AllArgsConstructor;
import models.dto.BookDTO;
import repositories.interfaces.BookRepository;
import repositories.interfaces.BookReservationRepository;
import repositories.interfaces.ReserveRepository;

import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
public class BookServiceImpl implements repositories.interfaces.serviceInterfaces.BookService {
    private BookRepository bookRepository;
    private ReserveRepository reserveRepository;
    public BookReservationRepository bookReservationRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BookDTO> getAllBooks() throws SQLException {
        return bookRepository.getAllBooks();
    }

    @Override
    public boolean isReservedByUser(int bookId, int userId) throws SQLException {
        return reserveRepository.isBookReservedByUser(bookId, userId);
    }

    @Override
    public boolean areCopiesAvailable(int bookId) throws SQLException {
        try {
            return bookReservationRepository.areCopiesAvailable(bookId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}