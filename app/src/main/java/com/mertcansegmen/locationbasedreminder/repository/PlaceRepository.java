package com.mertcansegmen.locationbasedreminder.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.persistence.AppDatabase;
import com.mertcansegmen.locationbasedreminder.persistence.BaseDao;
import com.mertcansegmen.locationbasedreminder.persistence.PlaceDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PlaceRepository extends BaseRepository<Place> {

    public PlaceRepository(Application application) {
        super();
        AppDatabase database = AppDatabase.getInstance(application);
        dao = database.placeDao();
        allItems = dao.getAll();
    }
}
