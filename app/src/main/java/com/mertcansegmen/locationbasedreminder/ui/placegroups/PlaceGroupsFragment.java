package com.mertcansegmen.locationbasedreminder.ui.placegroups;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroupWithPlaces;
import com.mertcansegmen.locationbasedreminder.util.Animator;

import java.util.List;

public class PlaceGroupsFragment extends Fragment {

    private FloatingActionButton addPlaceGroupButton;

    private PlaceGroupsFragmentViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_groups, container, false);

        addPlaceGroupButton = view.findViewById(R.id.btn_add_place_group);

        viewModel = ViewModelProviders.of(this).get(PlaceGroupsFragmentViewModel.class);

        Animator.animateFloatingActionButton(addPlaceGroupButton);

        viewModel.getAllPlaceGroupsWithPlaces().observe(this, new Observer<List<PlaceGroupWithPlaces>>() {
            @Override
            public void onChanged(List<PlaceGroupWithPlaces> placeGroupsWithPlaces) {
                // List play groups...
                for(PlaceGroupWithPlaces placeGroupWithPlaces : placeGroupsWithPlaces) {
                    Log.i("Mert", "onChanged: " + placeGroupWithPlaces);
                }
            }
        });

        return view;
    }

}
