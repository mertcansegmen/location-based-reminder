package com.mertcansegmen.locationbasedreminder.repository;

import androidx.lifecycle.LiveData;

import com.mertcansegmen.locationbasedreminder.persistence.BaseDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class BaseRepository<T> {

    protected BaseDao<T> dao;
    protected LiveData<List<T>> allItems;

    public void insert(T obj) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> dao.insert(obj));
    }

    public void update(T obj) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> dao.update(obj));
    }

    public void delete(T obj) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> dao.delete(obj));
    }

    public void deleteAll() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> dao.deleteAll());
    }

    public LiveData<List<T>> getAll() {
        return allItems;
    }

}
