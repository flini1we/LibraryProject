package repositories.interfaces;

import models.Genre;

import java.sql.SQLException;
import java.util.List;

public interface GenreRepository {
    void createGenre(Genre genre) throws SQLException;

    Genre getGenreById(int id) throws SQLException;

    List<Genre> getAllGenres() throws SQLException;

    void updateGenre(Genre genre) throws SQLException;

    void deleteGenre(int id) throws SQLException;
}
