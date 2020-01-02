package com.mertcansegmen.locationbasedreminder.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.mertcansegmen.locationbasedreminder.R;

public class OutlineChip extends Chip {
    public OutlineChip(Context context) {
        super(context);
        configureChip(context);
    }

    public OutlineChip(Context context, AttributeSet attrs) {
        super(context, attrs);
        configureChip(context);
    }

    public OutlineChip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        configureChip(context);
    }

    private void configureChip(Context context) {
        if(Utils.isDarkModeEnabled(context))
            setLightChip(context);
        else
            setDarkChip(context);
    }

    private void setLightChip(Context context) {
        styleChip(context, R.color.colorBlack, R.color.colorWhite);
    }

    private void setDarkChip(Context context) {
        styleChip(context, R.color.colorWhite, R.color.colorBlack);
    }

    private void styleChip(Context context, int backgroundColor, int borderIconTextColor) {
        setChipStartPadding(20);
        setTextStartPadding(3);
        setChipStrokeWidth(2);
        setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, borderIconTextColor)));
        setChipIconTintResource(borderIconTextColor);
        setChipStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(context, borderIconTextColor)));
        setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, backgroundColor)));
    }

}
