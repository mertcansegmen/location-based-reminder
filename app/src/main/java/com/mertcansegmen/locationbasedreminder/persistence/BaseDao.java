package com.mertcansegmen.locationbasedreminder.persistence;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface BaseDao<T> {

    long insert(T model);

    void update(T model);

    void delete(T model);

    void deleteAll();

    LiveData<List<T>> getAll();
}
