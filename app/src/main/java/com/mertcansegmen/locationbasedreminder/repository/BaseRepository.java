package com.mertcansegmen.locationbasedreminder.repository;

import androidx.lifecycle.LiveData;

import com.mertcansegmen.locationbasedreminder.persistence.BaseDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class BaseRepository<T> {

    protected BaseDao<T> dao;
    protected LiveData<List<T>> allItems;

    private Executor executor;

    public BaseRepository() {
        executor = Executors.newSingleThreadExecutor();
    }

    public void insert(T obj) { executor.execute(() -> dao.insert(obj)); }

    public void update(T obj) { executor.execute(() -> dao.update(obj)); }

    public void delete(T obj) { executor.execute(() -> dao.delete(obj)); }

    public void deleteAll() { executor.execute(() -> dao.deleteAll()); }

    public LiveData<List<T>> getAll() { return allItems; }

}
