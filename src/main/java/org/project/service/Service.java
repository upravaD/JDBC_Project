package org.project.service;

import java.util.List;

public interface Service<T> {
    T add(T value);
    T read(Long id);
    List<T> readAll();
    void update(T value);
    void remove(T value);
}
