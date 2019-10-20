package com.mertcansegmen.locationbasedreminder.util;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.chip.Chip;
import com.mertcansegmen.locationbasedreminder.model.Place;

public class PlaceChip extends Chip {

    private Place place;

    public PlaceChip(Context context) {
        super(context);
    }

    public PlaceChip(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaceChip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
