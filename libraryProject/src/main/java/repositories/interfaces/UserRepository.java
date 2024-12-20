package repositories.interfaces;

import models.User;
import repositories.crud.CrudRepository;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository extends CrudRepository<User> {
    void updateUserData(int userId, String username, String password) throws SQLException;

    int getIdByUsername(String username);

    User findByUsername(String username) throws SQLException;
}