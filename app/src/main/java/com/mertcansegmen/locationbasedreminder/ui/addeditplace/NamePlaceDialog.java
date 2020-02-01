package com.mertcansegmen.locationbasedreminder.ui.addeditplace;

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
import com.mertcansegmen.locationbasedreminder.viewmodel.NamePlaceDialogViewModel;

public class NamePlaceDialog extends DialogFragment {

    public static final String BUNDLE_KEY_PLACE = "com.mertcansegmen.locationbasedreminder.BUNDLE_KEY_PLACE";

    private MaterialButton okButton;
    private MaterialButton cancelButton;
    private TextInputLayout placeNameLayout;
    private TextInputEditText placeNameEditText;

    private Place currentPlace;

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

        retrievePlace();
        setOkButtonClickListener();
        setCancelButtonClickListener();
        setTextChangedListener();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
    }

    private void retrievePlace() {
        if (getArguments() != null && getArguments().getParcelable(BUNDLE_KEY_PLACE) != null) {
            currentPlace = getArguments().getParcelable(BUNDLE_KEY_PLACE);
            placeNameEditText.setText(currentPlace.getName());
        }
    }

    private void setOkButtonClickListener() {
        okButton.setOnClickListener(v -> {
            String placeName = placeNameEditText.getText().toString().trim();

            // Show error if place name field is empty
            if (placeName.isEmpty()) {
                placeNameLayout.setError(getString(R.string.error_empty_place_name));
                return;
            }

            currentPlace.setName(placeName);

            if (!inEditMode()) viewModel.insert(currentPlace);
            else viewModel.update(currentPlace);

            navController.popBackStack();
            navController.popBackStack();
        });
    }

    private void setCancelButtonClickListener() {
        cancelButton.setOnClickListener(v -> dismiss());
    }

    private void setTextChangedListener() {
        placeNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placeNameLayout.setErrorEnabled(false);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private boolean inEditMode() {
        return currentPlace.getPlaceId() != null;
    }
}
