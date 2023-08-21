package org.project.dao;

import org.project.model.Role;
import org.project.model.User;
import org.project.util.PostgresConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements DAO {

    private static final String INSERT_USER = "INSERT INTO users (username, role_id) VALUES (?, ?)";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String UPDATE_USER = "UPDATE users SET username = ?, role_id = ? WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM users WHERE id = ?";

    @Override
    public void create(User user) {

    }

    @Override
    public List<User> getALL() {
        List<User> users = new ArrayList<>();

        try (Connection connection = PostgresConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_USERS)) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));

                // Получение роли пользователя по role_id из базы данных
                int roleId = resultSet.getInt("role_id");
                RoleDAO roleDAO = new RoleDAO();
                Role role = roleDAO.findById(roleId);

                user.setRole(role);

                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User findByID() {
        return null;
    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }
}
