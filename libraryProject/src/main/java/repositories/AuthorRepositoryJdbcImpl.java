package repositories;

import models.Author;
import repositories.interfaces.AuthorRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepositoryJdbcImpl implements AuthorRepository {
    private final Connection connection;

    private static final String INSERT_AUTHOR = "INSERT INTO authors (name) VALUES (?)";
    private static final String SELECT_AUTHOR_BY_ID = "SELECT * FROM authors WHERE id = ?";
    private static final String SELECT_ALL_AUTHORS = "SELECT * FROM authors";
    private static final String UPDATE_AUTHOR = "UPDATE authors SET name = ? WHERE id = ?";
    private static final String DELETE_AUTHOR = "DELETE FROM authors WHERE id = ?";

    public AuthorRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createAuthor(Author author) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_AUTHOR)) {
            stmt.setString(1, author.getName());
            stmt.executeUpdate();
        }
    }

    @Override
    public Author getAuthorById(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_AUTHOR_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToAuthor(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Author> getAllAuthors() throws SQLException {
        List<Author> authors = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_AUTHORS)) {
            while (rs.next()) {
                authors.add(mapToAuthor(rs));
            }
        }
        return authors;
    }

    @Override
    public void updateAuthor(Author author) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_AUTHOR)) {
            stmt.setString(1, author.getName());
            stmt.setInt(2, author.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteAuthor(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_AUTHOR)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Author mapToAuthor(ResultSet rs) throws SQLException {
        Author author = new Author();
        author.setId(rs.getInt("id"));
        author.setName(rs.getString("name"));
        return author;
    }
}
