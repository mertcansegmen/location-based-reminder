package com.mertcansegmen.locationbasedreminder.ui.addeditplacegroup;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.mertcansegmen.locationbasedreminder.model.PlaceGroupWithPlaces;
import com.mertcansegmen.locationbasedreminder.repository.PlaceGroupRepository;

public class AddEditPlaceGroupFragmentViewModel extends AndroidViewModel {

    private PlaceGroupRepository repository;

    public AddEditPlaceGroupFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new PlaceGroupRepository(application);
    }

    public void insert(PlaceGroupWithPlaces placeGroupWithPlaces) {
        repository.insert(placeGroupWithPlaces);
    }

    public void update(PlaceGroupWithPlaces placeGroupWithPlaces) {
        repository.update(placeGroupWithPlaces);
    }

    public void delete(PlaceGroupWithPlaces placeGroupWithPlaces) {
        repository.delete(placeGroupWithPlaces);
    }
}
