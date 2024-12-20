package repositories.interfaces.serviceInterfaces;

import java.sql.SQLException;

public interface UserService {
    boolean doUserHaveAnyReservedBooks(int userId) throws SQLException;

}
