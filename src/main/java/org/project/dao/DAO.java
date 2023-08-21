package org.project.dao;

import org.project.model.User;

import java.util.List;

public interface DAO {
    void create(User user);
    List<User> getALL();
    User findByID();
    void update();
    void delete();
}
