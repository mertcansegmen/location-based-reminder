package com.mertcansegmen.locationbasedreminder.ui.addeditplace;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mertcansegmen.locationbasedreminder.R;

public class NamePlaceDialog extends DialogFragment {

    MaterialButton okButton;
    MaterialButton cancelButton;
    TextInputLayout placeNameLayout;
    TextInputEditText placeNameEditText;

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

        //Bundle bundle = getArguments();

        //Place place = bundle.getParcelable("place");

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add place name to database
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete place object from database
                dismiss();
            }
        });

        return dialog.create();
    }
}
