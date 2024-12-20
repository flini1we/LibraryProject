package service;

import lombok.AllArgsConstructor;
import repositories.interfaces.BookReservationRepository;
import repositories.interfaces.ReserveRepository;
import repositories.interfaces.serviceInterfaces.ReserveService;
import java.sql.SQLException;

@AllArgsConstructor
public class ReserveServiceImpl implements ReserveService {
    private final ReserveRepository reserveRepository;
    private final BookReservationRepository bookReservationRepository;

    @Override
    public boolean isReservedByUser(int bookId, int userId) {
        return reserveRepository.isBookReservedByUser(bookId, userId);
    }

    @Override
    public boolean areCopiesAvailable(int bookId) {
        try {
            return bookReservationRepository.areCopiesAvailable(bookId);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при проверке доступности экземпляров книги", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
