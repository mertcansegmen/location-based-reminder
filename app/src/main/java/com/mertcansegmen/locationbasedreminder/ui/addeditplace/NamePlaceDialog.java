package com.mertcansegmen.locationbasedreminder.ui.addeditplace;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

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

    private NamePlaceDialogViewModel viewModel;

    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_name_place, null);
        dialog.setView(view).setTitle(getString(R.string.name_this_place));

        okButton = view.findViewById(R.id.btn_ok);
        cancelButton = view.findViewById(R.id.btn_cancel);
        placeNameEditText = view.findViewById(R.id.edittext_place_name);
        placeNameLayout = view.findViewById(R.id.edittext_layout_place_name);

        viewModel = ViewModelProviders.of(this).get(NamePlaceDialogViewModel.class);

        Bundle bundle = getArguments();
        final Place place = bundle.getParcelable(BUNDLE_KEY_PLACE);

        if(!isNewPlace(place)) {
            placeNameEditText.setText(place.getName());
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeName = placeNameEditText.getText().toString().trim();
                place.setName(placeName);

                // Show error if place name field is empty
                if(placeName.isEmpty()) {
                    placeNameLayout.setError(getString(R.string.error_empty_place_name));
                    return;
                }

                if(isNewPlace(place)) {
                    viewModel.insert(place);
                } else {
                    viewModel.update(place);
                }
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

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

        return dialog.create();
    }

    private boolean isNewPlace(Place place) {
        return place.getİd() == null;
    }
}
