package org.project.dao;

import java.util.List;

public interface DAO<T> {
    void create(T value);
    List<T> getALL();
    T findByID(int id);
    void update(T value);
    void delete(T value);
}
