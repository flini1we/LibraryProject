package repositories.interfaces.serviceInterfaces;

public interface ReserveService {
    boolean isReservedByUser(int bookId, int userId);

    boolean areCopiesAvailable(int bookId);
}
