package com.mertcansegmen.locationbasedreminder.ui.addeditplace;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Place;

public class NamePlaceDialog extends DialogFragment {

    public static final String EXTRA_PLACE = "com.mertcansegmen.locationbasedreminder.EXTRA_PLACE";

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

        dialog.setView(view).setTitle("Name this place");

        okButton = view.findViewById(R.id.btn_ok);
        cancelButton = view.findViewById(R.id.btn_cancel);
        placeNameEditText = view.findViewById(R.id.edittext_place_name);
        placeNameLayout = view.findViewById(R.id.edittext_layout_place_name);

        viewModel = ViewModelProviders.of(this).get(NamePlaceDialogViewModel.class);

        Bundle bundle = getArguments();
        final Place place = bundle.getParcelable(EXTRA_PLACE);

        if(!isNewPlace(place)) {
            placeNameEditText.setText(place.getName());
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNewPlace(place)) {
                    insertNewPlace(place);
                } else {
                    updatePlace(place);
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

        return dialog.create();
    }

    private void insertNewPlace(Place place) {
        String placeName = placeNameEditText.getText().toString();
        place.setName(placeName);
        viewModel.insert(place);
    }

    private void updatePlace(Place place) {
        String placeName = placeNameEditText.getText().toString();
        place.setName(placeName);
        viewModel.update(place);
    }

    private boolean isNewPlace(Place place) {
        return place.getİd() == null;
    }
}
