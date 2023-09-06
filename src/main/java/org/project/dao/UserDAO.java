package org.project.dao;

import org.project.model.Role;
import org.project.model.User;
import org.project.util.Queries;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements DAO<User> {

    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(User user) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(Queries.USER_CREATE.get())) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setLong(2, user.getRole().getId());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            resultSet.next();
            user.setId(resultSet.getLong("id"));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<User> getALL() {
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(Queries.USER_GET_ALL.get())) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                RoleDAO roleDAO = new RoleDAO(connection);
                Role role = roleDAO.findByID(resultSet.getLong("role_id"));
                user.setRole(role);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User findByID(Long id) {
        User user = null;
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(Queries.USER_FIND_BY_ID.get())) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setRole(new RoleDAO(connection).findByID(resultSet.getLong("role_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (user == null) {
            user = new User();
            user.setId(-1L);
            user.setUsername("emptyUserName");
            user.setRole(new RoleDAO(connection).findByID(-1L));
        }
        return user;
    }

    @Override
    public boolean update(User user) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(Queries.USER_UPDATE.get())) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setLong(2, user.getRole().getId());
            preparedStatement.setLong(3, user.getId());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(User user) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(Queries.USER_DELETE.get())) {
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
