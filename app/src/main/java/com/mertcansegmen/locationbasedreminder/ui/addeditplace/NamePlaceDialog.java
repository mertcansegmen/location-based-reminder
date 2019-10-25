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

    private MaterialButton okButton;
    private MaterialButton cancelButton;
    private TextInputLayout placeNameLayout;
    private TextInputEditText placeNameEditText;

    private NamePlaceDialogViewModel viewModel;

    @Nullable
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_name_place, null);

        dialog.setView(view).setTitle("Name this place");

        okButton = view.findViewById(R.id.btn_ok);
        cancelButton = view.findViewById(R.id.btn_cancel);
        placeNameEditText = view.findViewById(R.id.edittext_place_name);
        placeNameLayout = view.findViewById(R.id.edittext_layout_place_name);

        viewModel = ViewModelProviders.of(this).get(NamePlaceDialogViewModel.class);

        final Bundle bundle = getArguments();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeName = placeNameEditText.getText().toString();
                double lat = bundle.getDouble("lat");
                double lng = bundle.getDouble("lng");
                Place place = new Place(placeName, lat, lng);
                viewModel.insert(place);
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
}
