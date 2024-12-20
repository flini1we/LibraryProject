package repositories;

import models.User;
import repositories.interfaces.UserRepository;
import utils.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryJdbcImpl implements UserRepository {
    private final Connection connection;

    private static final String INSERT_USER = "INSERT INTO users (username, password, email, role, registration_date) VALUES (?, ?, ?, ?::user_role, ?)";
    private static final String UPDATE_USER_DATA = "UPDATE users SET username = ?, password = ? WHERE id = ?";
    private static final String GET_USER_ID = "SELECT * FROM users WHERE username = ?";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String UPDATE_USER = "UPDATE users SET username = ?, password = ?, email = ?, role = ? WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM users WHERE id = ?";
    private static final String SELECT_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
    private static final String IF_USER_EXISTS = "SELECT COUNT(*) FROM users WHERE username = ? OR email = ?";

    public UserRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(User entity) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_USER)) {
            stmt.setString(1, entity.getUsername());
            stmt.setString(2, entity.getPassword());
            stmt.setString(3, entity.getEmail());
            stmt.setString(4, entity.getRole());
            stmt.setTimestamp(5, Timestamp.valueOf(entity.getRegistrationDate()));
            stmt.executeUpdate();
        }
    }

    @Override
    public User getById(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_USER_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToUser(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void update(User entity) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_USER)) {
            stmt.setString(1, entity.getUsername());
            stmt.setString(2, entity.getPassword());
            stmt.setString(3, entity.getEmail());
            stmt.setString(4, entity.getRole());
            stmt.setInt(5, entity.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_USER)) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    @Override
    public void updateUserData(int userId, String username, String password) throws SQLException {
        String hashedPassword = PasswordUtil.hashPassword(password);
        PreparedStatement statement = connection.prepareStatement(UPDATE_USER_DATA);

        statement.setString(1, username);
        statement.setString(2, hashedPassword);
        statement.setInt(3, userId);

        statement.executeUpdate();
    }

    @Override
    public int getIdByUsername(String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_ID)) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<User> getAll() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_USERS)) {
            while (rs.next()) {
                users.add(mapToUser(rs));
            }
        }
        return users;
    }

    @Override
    public User findByUsername(String username) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_USERNAME)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapToUser(rs);
                }
            }
        }
        return null;
    }

    private User mapToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        user.setRegistrationDate(rs.getTimestamp("registration_date").toLocalDateTime());
        return user;
    }

    public boolean userExists(String username, String email) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(IF_USER_EXISTS)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}