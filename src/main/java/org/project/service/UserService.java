package org.project.service;

import org.project.dao.UserDAO;
import org.project.model.User;

import java.util.List;

public class UserService implements Service<User>{
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    @Override
    public void add(User user) {
        userDAO.create(user);
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
