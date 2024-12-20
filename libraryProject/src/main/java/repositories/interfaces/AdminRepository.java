package repositories.interfaces;

import models.User;
import repositories.crud.CrudRepository;
import repositories.interfaces.serviceInterfaces.AdminService;

import java.sql.SQLException;

public interface AdminRepository extends CrudRepository<User>, AdminService {
    int getIdByUsername(String username);

    User findByUsername(String username) throws SQLException;

    void promoteOrDemoteUser(User user) throws SQLException;
}
