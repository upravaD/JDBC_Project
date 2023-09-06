package org.project.dao;

import java.util.List;

public interface DAO<T> {
    boolean create(T value);
    List<T> getALL();
    T findByID(Long id);
    boolean update(T value);
    boolean delete(T value);
}
