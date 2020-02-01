package com.mertcansegmen.locationbasedreminder.repository;

import android.app.Application;

import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.persistence.AppDatabase;

public class PlaceRepository extends BaseRepository<Place> {

    public PlaceRepository(Application application) {
        super();
        AppDatabase database = AppDatabase.getInstance(application);
        dao = database.placeDao();
        allItems = dao.getAll();
    }
}
