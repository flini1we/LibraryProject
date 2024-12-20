package repositories.interfaces;

import models.Author;

import java.sql.SQLException;
import java.util.List;

public interface AuthorRepository {
    void createAuthor(Author author) throws SQLException;

    Author getAuthorById(int id) throws SQLException;

    List<Author> getAllAuthors() throws SQLException;

    void updateAuthor(Author author) throws SQLException;

    void deleteAuthor(int id) throws SQLException;
}
