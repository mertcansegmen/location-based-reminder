package com.mertcansegmen.locationbasedreminder.ui.addeditplace;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.repository.PlaceRepository;

public class AddEditPlaceFragmentViewModel extends AndroidViewModel {

    private PlaceRepository repository;

    private LatLng lastKnownScreenLocation;
    private Float lastKnownZoom;
    private int radius;

    public AddEditPlaceFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new PlaceRepository(application);
    }

    public void delete(Place place) {
        repository.delete(place);
    }

    public LatLng getLastKnownScreenLocation() {
        return lastKnownScreenLocation;
    }

    public void setLastKnownScreenLocation(LatLng lastKnownScreenLocation) {
        this.lastKnownScreenLocation = lastKnownScreenLocation;
    }

    public Float getLastKnownZoom() {
        return lastKnownZoom;
    }

    public void setLastKnownZoom(Float lastKnownZoom) {
        this.lastKnownZoom = lastKnownZoom;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
