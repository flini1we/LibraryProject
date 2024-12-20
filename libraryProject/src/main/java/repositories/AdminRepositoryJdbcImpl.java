package repositories;

import models.User;
import repositories.interfaces.AdminRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminRepositoryJdbcImpl implements AdminRepository {
    private final Connection connection;

    private static final String SELECT_ADMINS = "SELECT * FROM users WHERE role = 'ADMIN'";
    private static final String CREATE_ADMIN = "INSERT INTO users (username, password, email, role, registration_date) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_ADMIN_BY_ID = "SELECT * FROM users WHERE id = ? AND role = 'ADMIN'";
    private static final String FIND_BY_USERNAME = "SELECT * FROM users WHERE username = ? AND role = 'ADMIN'";
    private static final String UPDATE_ADMIN = "UPDATE users SET username = ?, password = ?, email = ?, role = ? WHERE id = ?";
    private static final String DELETE_ADMIN = "DELETE FROM users WHERE id = ? AND role = 'ADMIN'";
    private static final String PROMOTE_OR_DEMOTE_USER = "UPDATE USERS SET ROLE = ?::user_role WHERE id = ?";
    private static final String GET_ALL_USERS_EXCEPT_SELF = "SELECT id, username,password, email, role, registration_date FROM users WHERE id != ?";

    public AdminRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(User entity) throws SQLException {
        if (!"ADMIN".equals(entity.getRole())) {
            return;
        }
        try (PreparedStatement stmt = connection.prepareStatement(CREATE_ADMIN)) {
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
        try (PreparedStatement stmt = connection.prepareStatement(GET_ADMIN_BY_ID)) {
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
    public int getIdByUsername(String username) {
        return 0;
    }

    @Override
    public User findByUsername(String username) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(FIND_BY_USERNAME)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToUser(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void promoteOrDemoteUser(User user) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(PROMOTE_OR_DEMOTE_USER)) {
            String role = user.getRole();
            String updatedRole = role.equals("ADMIN") ? "READER" : "ADMIN";

            statement.setString(1, updatedRole);
            statement.setInt(2, user.getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                user.setRole(updatedRole);
            } else {
                throw new SQLException("Не удалось обновить роль пользователя с ID: " + user.getId());
            }
        }
    }

    @Override
    public List<User> getAll() throws SQLException {
        List<User> admins = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_ADMINS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                admins.add(mapToUser(rs));
            }
        }
        return admins;
    }

    @Override
    public void update(User entity) throws SQLException {
        if (!"ADMIN".equals(entity.getRole())) {
            return;
        }
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_ADMIN)) {
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
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_ADMIN)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
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

    @Override
    public List<User> getAllUsersExceptSelf(int adminId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(GET_ALL_USERS_EXCEPT_SELF)) {
            stmt.setInt(1, adminId);
            List<User> users = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapToUser(rs));
                }
                return users;
            }
        }
    }
}