package repositories.interfaces.serviceInterfaces;

import models.User;

import java.sql.SQLException;
import java.util.List;

public interface AdminService {
    List<User> getAllUsersExceptSelf(int adminId) throws SQLException;
}
