package repositories;

import models.Author;
import models.Book;
import models.Genre;
import models.Review;
import models.dto.BookDTO;
import repositories.interfaces.AuthorRepository;
import repositories.interfaces.BookRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepositoryJdbcImpl implements BookRepository {
    private final Connection connection;

    private static final String INSERT_BOOK = "INSERT INTO books (title, author_id, genre_id, description, publication_year, isbn, available_copies, total_copies) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BOOK_BY_ID = "SELECT b.*, a.name AS author_name, g.name AS genre_name " +
            "FROM books b " +
            "JOIN authors a ON b.author_id = a.id " +
            "JOIN genres g ON b.genre_id = g.id " +
            "WHERE b.id = ?";
    private static final String UPDATE_BOOK = "UPDATE books SET title = ?, author_id = ?, genre_id = ?, description = ?, publication_year = ?, isbn = ?, available_copies = ?, total_copies = ? WHERE id = ?";
    private static final String DELETE_BOOK = "DELETE FROM books WHERE id = ?";
    private static final String SELECT_ALL_BOOKS_INTO_TABLE =
            "SELECT b.id, b.title, a.name AS author_name, b.publication_year, b.description, " +
            "i.image_path, b.isbn, b.available_copies, b.total_copies " +
            "FROM books b " +
            "JOIN book_authors ba ON b.id = ba.book_id " +
            "JOIN authors a ON ba.author_id = a.id " +
            "LEFT JOIN images i ON b.id = i.book_id";
    private static final String IS_RESERVED_BY_USER = "SELECT * FROM book_reservations WHERE book_id = ? AND user_id = ? AND status = 'Active'";
    private static final String GET_REVIEWS = "SELECT r.id, r.user_id, u.username, r.rating, r.comment, r.review_date " +
            "FROM reviews r JOIN users u ON r.user_id = u.id WHERE r.book_id = ?";
    private static final String SELECT_BY_ID_TO_BOOK_DTO = "SELECT b.id AS book_id, b.title, a.name AS author_name, b.publication_year, b.description, b.isbn, b.available_copies, b.total_copies, i.image_path FROM books b JOIN authors a ON b.author_id = a.id LEFT JOIN images i ON b.id = i.book_id WHERE b.id = ?";
    private static final String INCREASE_BOOK_COPIES = "UPDATE books SET available_copies = available_copies + 1, total_copies = total_copies + 1 WHERE id = ?";
    private static final String DECREASE_BOOK_COPIES = "UPDATE books SET available_copies = available_copies - 1, total_copies = total_copies - 1 WHERE id = ? AND available_copies > 0 AND total_copies > 0";

    public BookRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createBook(Book book) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_BOOK)) {
            statement.setString(1, book.getTitle());
            statement.setInt(2, book.getAuthor().getId());
            statement.setInt(3, book.getGenre().getId());
            statement.setString(4, book.getDescription());
            statement.setInt(5, book.getPublicationYear());
            statement.setString(6, book.getIsbn());
            statement.setInt(7, book.getAvailableCopies());
            statement.setInt(8, book.getTotalCopies());
            statement.executeUpdate();
        }
    }

    @Override
    public Book getBookById(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BOOK_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToBook(rs);
                }
            }
        }
        return null;
    }

    @Override
    public BookDTO findBookDTOById(int bookId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_TO_BOOK_DTO)) {
            preparedStatement.setInt(1, bookId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    BookDTO bookDTO = new BookDTO();
                    bookDTO.setId(resultSet.getInt("book_id"));
                    bookDTO.setTitle(resultSet.getString("title"));
                    bookDTO.setAuthorName(resultSet.getString("author_name"));
                    bookDTO.setPublicationYear(resultSet.getInt("publication_year"));
                    bookDTO.setDescription(resultSet.getString("description"));
                    bookDTO.setIsbn(resultSet.getString("isbn"));
                    bookDTO.setAvailableCopies(resultSet.getInt("available_copies"));
                    bookDTO.setTotalCopies(resultSet.getInt("total_copies"));
                    bookDTO.setImagePath(resultSet.getString("image_path"));
                    bookDTO.setAvailableCopiesPresent(resultSet.getInt("available_copies") > 0);

                    return bookDTO;
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public List<BookDTO> getAllBooks() throws SQLException {
        List<BookDTO> books = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_BOOKS_INTO_TABLE)) {
            while (rs.next()) {
                books.add(mapToBookDTO(rs));
            }
        }
        return books;
    }

    @Override
    public void updateBook(Book book) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_BOOK)) {
            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getAuthor().getId());
            stmt.setInt(3, book.getGenre().getId());
            stmt.setString(4, book.getDescription());
            stmt.setInt(5, book.getPublicationYear());
            stmt.setString(6, book.getIsbn());
            stmt.setInt(7, book.getAvailableCopies());
            stmt.setInt(8, book.getTotalCopies());
            stmt.setInt(9, book.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteBook(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_BOOK)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean isReservedByUser(int bookId, int userId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(IS_RESERVED_BY_USER)) {
            statement.setInt(1, bookId);
            statement.setInt(2, userId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Review> getReviewsForBook(int bookId) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(GET_REVIEWS)) {
            statement.setInt(1, bookId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int reviewId = resultSet.getInt("id");
                    int userId = resultSet.getInt("user_id");
                    String username = resultSet.getString("username");
                    int rating = resultSet.getInt("rating");
                    String reviewText = resultSet.getString("comment");
                    Timestamp reviewDate = resultSet.getTimestamp("review_date");

                    Review review = new Review(reviewId, bookId, userId, username, null, rating, reviewText, reviewDate);
                    reviews.add(review);
                }
            }
        }
        return reviews;
    }

    @Override
    public boolean increaseBookCopies(int bookId) throws SQLException {
        return updateBookCopies(bookId, INCREASE_BOOK_COPIES);
    }

    @Override
    public boolean decreaseBookCopies(int bookId) throws SQLException {
        Book book = this.getBookById(bookId);
        if (book.getAvailableCopies() == 0) {
            return false;
        }
        return updateBookCopies(bookId, DECREASE_BOOK_COPIES);
    }

    private boolean updateBookCopies(int bookId, String query) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    private Book mapToBook(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getInt("id"));
        book.setTitle(resultSet.getString("title"));

        Author author = new Author();
        author.setId(resultSet.getInt("author_id"));
        author.setName(resultSet.getString("author_name"));
        book.setAuthor(author);

        Genre genre = new Genre();
        genre.setId(resultSet.getInt("genre_id"));
        genre.setName(resultSet.getString("genre_name"));
        book.setGenre(genre);

        book.setDescription(resultSet.getString("description"));
        book.setPublicationYear(resultSet.getInt("publication_year"));
        book.setIsbn(resultSet.getString("isbn"));
        book.setAvailableCopies(resultSet.getInt("available_copies"));
        book.setTotalCopies(resultSet.getInt("total_copies"));
        return book;
    }

    private BookDTO mapToBookDTO(ResultSet resultSet) throws SQLException {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(resultSet.getInt("id"));
        bookDTO.setTitle(resultSet.getString("title"));
        bookDTO.setAuthorName(resultSet.getString("author_name"));
        bookDTO.setDescription(resultSet.getString("description"));
        bookDTO.setPublicationYear(resultSet.getInt("publication_year"));
        bookDTO.setIsbn(resultSet.getString("isbn"));
        bookDTO.setAvailableCopies(resultSet.getInt("available_copies"));
        bookDTO.setTotalCopies(resultSet.getInt("total_copies"));
        bookDTO.setImagePath(resultSet.getString("image_path"));
        return bookDTO;
    }
}