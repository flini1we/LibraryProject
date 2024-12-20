package repositories.crud;

import java.sql.SQLException;
import java.util.List;

public interface CrudRepository<T> {
    void create(T entity) throws SQLException;

    T getById(int id) throws SQLException;

    List<T> getAll() throws SQLException;

    void update(T entity) throws SQLException;

    boolean delete(int id) throws SQLException;
}