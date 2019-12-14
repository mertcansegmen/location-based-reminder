package com.mertcansegmen.locationbasedreminder.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Place;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PlaceChip extends Chip {

    private Place place;

    private List<Integer> colors = Arrays.asList(
            R.color.colorChipRed,
            R.color.colorChipPink,
            R.color.colorChipPurple,
            R.color.colorChipDeepPurple,
            R.color.colorChipIndigo,
            R.color.colorChipBlue,
            R.color.colorChipLightBlue,
            R.color.colorChipCyan,
            R.color.colorChipTeal,
            R.color.colorChipGreen,
            R.color.colorChipLightGreen,
            R.color.colorChipLime,
            R.color.colorChipYellow,
            R.color.colorChipAmber,
            R.color.colorChipOrange,
            R.color.colorChipDeepOrange,
            R.color.colorChipBrown,
            R.color.colorChipBlueGray
    );

    public PlaceChip(Context context) {
        super(context);
        configureChipColors(context);
    }

    public PlaceChip(Context context, AttributeSet attrs) {
        super(context, attrs);
        configureChipColors(context);
    }

    public PlaceChip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        configureChipColors(context);
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    private void configureChipColors(Context context) {
        setRandomChipColor(context);
        setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorBlack)));
    }

    private void setRandomChipColor(Context context) {
        Random random = new Random();
        int randomIndex = random.nextInt(colors.size());
        int color = colors.get(randomIndex);
        setChipBackgroundColor(ColorStateList.valueOf(ContextCompat
                .getColor(context, color)));
    }
}
