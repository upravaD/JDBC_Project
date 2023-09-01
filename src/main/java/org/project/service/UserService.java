package org.project.service;

import org.project.dao.UserDAO;
import org.project.model.User;

import java.sql.Connection;
import java.util.List;

public class UserService implements Service<User>{
    private final UserDAO userDAO;

    public UserService(Connection connection) {
        this.userDAO = new UserDAO(connection);
    }

    @Override
    public User add(User user) {
        userDAO.create(user);
        return userDAO.findByID(user.getId());
    }

    @Override
    public User read(Long id) {
        return userDAO.findByID(id);
    }

    @Override
    public List<User> readAll() {
        return userDAO.getALL();
    }

    @Override
    public void update(User user) {
        userDAO.update(user);
    }

    @Override
    public void remove(User user) {
        userDAO.delete(user);
    }
}
