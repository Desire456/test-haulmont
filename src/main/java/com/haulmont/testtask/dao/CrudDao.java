package com.haulmont.testtask.dao;

import java.util.List;

public interface CrudDao<T> {
    void save(T t);

    void update(T t);

    void remove(T t);

    List<T> getAll();
}
