package com.haulmont.testtask.services;

import java.util.Collection;
import java.util.List;

public interface CrudService<T> {
    void save(T t);

    void update(T t);

    void remove(T t) throws RemoveEntityException;

    List<T> getAll();

    void remove(Collection<T> collection) throws RemoveEntityException;
}
