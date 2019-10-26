package com.mertcansegmen.locationbasedreminder.ui.placegroups;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.util.Animator;

public class PlaceGroupsFragment extends Fragment {

    FloatingActionButton addPlaceGroupButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_groups, container, false);

        addPlaceGroupButton = view.findViewById(R.id.btn_add_place_group);

        Animator.animateFloatingActionButton(addPlaceGroupButton);

        return view;
    }

}
