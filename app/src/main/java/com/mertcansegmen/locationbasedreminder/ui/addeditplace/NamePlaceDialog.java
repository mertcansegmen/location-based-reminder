package com.mertcansegmen.locationbasedreminder.ui.addeditplace;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Place;

public class NamePlaceDialog extends DialogFragment {

    public static final String BUNDLE_KEY_PLACE = "com.mertcansegmen.locationbasedreminder.BUNDLE_KEY_PLACE";

    private MaterialButton okButton;
    private MaterialButton cancelButton;
    private TextInputLayout placeNameLayout;
    private TextInputEditText placeNameEditText;

    Place currentPlace;

    private NamePlaceDialogViewModel viewModel;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_name_place, container, false);

        okButton = view.findViewById(R.id.btn_ok);
        cancelButton = view.findViewById(R.id.btn_cancel);
        placeNameEditText = view.findViewById(R.id.edittext_place_name);
        placeNameLayout = view.findViewById(R.id.edittext_layout_place_name);

        viewModel = ViewModelProviders.of(this).get(NamePlaceDialogViewModel.class);

        if(getArguments() != null) {
            currentPlace = getArguments().getParcelable(BUNDLE_KEY_PLACE);
        }

        if(!isNewPlace(currentPlace)) {
            placeNameEditText.setText(currentPlace.getName());
        }

        okButton.setOnClickListener(v -> {
            String placeName = placeNameEditText.getText().toString().trim();
            currentPlace.setName(placeName);

            // Show error if place name field is empty
            if(placeName.isEmpty()) {
                placeNameLayout.setError(getString(R.string.error_empty_place_name));
                return;
            }

            if(isNewPlace(currentPlace)) {
                viewModel.insert(currentPlace);
            } else {
                viewModel.update(currentPlace);
            }

            navController.navigate(R.id.action_namePlaceDialog_to_placesFragment);
        });

        cancelButton.setOnClickListener(v -> dismiss());

        placeNameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placeNameLayout.setErrorEnabled(false);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
    }

    private boolean isNewPlace(Place place) {
        return place.getPlaceId() == null;
    }
}
