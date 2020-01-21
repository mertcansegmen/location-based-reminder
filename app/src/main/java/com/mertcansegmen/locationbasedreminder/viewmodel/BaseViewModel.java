package com.mertcansegmen.locationbasedreminder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mertcansegmen.locationbasedreminder.repository.BaseRepository;

import java.util.List;

public abstract class BaseViewModel<T> extends AndroidViewModel {

    protected BaseRepository<T> repository;
    protected LiveData<List<T>> allItems;

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public void insert(T obj) {
        repository.insert(obj);
    }

    public void update(T obj) {
        repository.update(obj);
    }

    public void delete(T obj) {
        repository.delete(obj);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<List<T>> getAll() {
        return allItems;
    }
}
