package com.mertcansegmen.locationbasedreminder.ui.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Place;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ColorfulChip extends Chip {

    private Place place;

    private static final List<Integer> colors = Arrays.asList(
            R.color.colorChipRed300,
            R.color.colorChipPink300,
            R.color.colorChipPurple300,
            R.color.colorChipDeepPurple300,
            R.color.colorChipIndigo300,
            R.color.colorChipBlue300,
            R.color.colorChipLightBlue300,
            R.color.colorChipCyan300,
            R.color.colorChipTeal300,
            R.color.colorChipGreen300,
            R.color.colorChipLightGreen300,
            R.color.colorChipLime300,
            R.color.colorChipYellow300,
            R.color.colorChipAmber300,
            R.color.colorChipOrange300,
            R.color.colorChipDeepOrange300,
            R.color.colorChipBrown300,
            R.color.colorChipBlueGray300,
            R.color.colorChipRed200,
            R.color.colorChipPink200,
            R.color.colorChipPurple200,
            R.color.colorChipDeepPurple200,
            R.color.colorChipIndigo200,
            R.color.colorChipBlue200,
            R.color.colorChipLightBlue200,
            R.color.colorChipCyan200,
            R.color.colorChipTeal200,
            R.color.colorChipGreen200,
            R.color.colorChipLightGreen200,
            R.color.colorChipLime200,
            R.color.colorChipYellow200,
            R.color.colorChipAmber200,
            R.color.colorChipOrange200,
            R.color.colorChipDeepOrange200,
            R.color.colorChipBrown200,
            R.color.colorChipBlueGray200,
            R.color.colorChipRed100,
            R.color.colorChipPink100,
            R.color.colorChipPurple100,
            R.color.colorChipDeepPurple100,
            R.color.colorChipIndigo100,
            R.color.colorChipBlue100,
            R.color.colorChipLightBlue100,
            R.color.colorChipCyan100,
            R.color.colorChipTeal100,
            R.color.colorChipGreen100,
            R.color.colorChipLightGreen100,
            R.color.colorChipLime100,
            R.color.colorChipYellow100,
            R.color.colorChipAmber100,
            R.color.colorChipOrange100,
            R.color.colorChipDeepOrange100,
            R.color.colorChipBrown100,
            R.color.colorChipBlueGray100
    );

    public ColorfulChip(Context context) {
        super(context);
        configureChip(context);
    }

    public ColorfulChip(Context context, AttributeSet attrs) {
        super(context, attrs);
        configureChip(context);
    }

    public ColorfulChip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        configureChip(context);
    }

    private void configureChip(Context context) {
        setRandomChipColor(context);
        setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorBlack)));
        setTextStartPadding(3);
        setClickable(false);
        setRippleColor(ColorStateList.valueOf(Color.TRANSPARENT));
    }

    private void setRandomChipColor(Context context) {
        Random random = new Random();
        int randomIndex = random.nextInt(colors.size());
        int color = colors.get(randomIndex);
        setChipBackgroundColor(ColorStateList.valueOf(ContextCompat
                .getColor(context, color)));
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
