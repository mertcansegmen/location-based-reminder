package com.mertcansegmen.locationbasedreminder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.repository.PlaceRepository;

public class AddEditPlaceFragmentViewModel extends BaseViewModel<Place> {

    private LatLng lastKnownScreenLocation;
    private Float lastKnownZoom;
    private int radius;

    public AddEditPlaceFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new PlaceRepository(application);
        allItems = repository.getAll();
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
