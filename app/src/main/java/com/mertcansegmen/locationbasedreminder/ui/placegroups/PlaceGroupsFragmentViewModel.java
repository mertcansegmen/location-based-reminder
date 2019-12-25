package com.mertcansegmen.locationbasedreminder.ui.placegroups;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mertcansegmen.locationbasedreminder.model.PlaceGroupWithPlaces;
import com.mertcansegmen.locationbasedreminder.repository.PlaceGroupRepository;

import java.util.List;

public class PlaceGroupsFragmentViewModel extends AndroidViewModel {

    private PlaceGroupRepository repository;
    private LiveData<List<PlaceGroupWithPlaces>> allPlaceGroupsWithPlaces;

    public PlaceGroupsFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new PlaceGroupRepository(application);
        allPlaceGroupsWithPlaces = repository.getAllPlaceGroupsWithPlaces();
    }

    public void insert(PlaceGroupWithPlaces placeGroupWithPlaces) {
        repository.insert(placeGroupWithPlaces);
    }


    public void delete(PlaceGroupWithPlaces placeGroupWithPlaces) {
        repository.delete(placeGroupWithPlaces);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<List<PlaceGroupWithPlaces>> getAllPlaceGroupsWithPlaces() {
        return allPlaceGroupsWithPlaces;
    }
}