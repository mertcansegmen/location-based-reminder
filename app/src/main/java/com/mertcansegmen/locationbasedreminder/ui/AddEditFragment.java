package com.mertcansegmen.locationbasedreminder.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.util.ConfigUtils;

public abstract class AddEditFragment extends BaseFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (inEditMode()) {
            inflater.inflate(R.menu.edit_menu, menu);
        } else {
            inflater.inflate(R.menu.add_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveMenuItemClicked();
                return true;
            case R.id.delete:
                askToDeleteItem();
                return true;
            case R.id.add_to_reminder:
                addToReminderMenuItemClicked();
                return true;
            case android.R.id.home:
                navController.popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected abstract void saveMenuItemClicked();

    private void askToDeleteItem() {
        new MaterialAlertDialogBuilder(requireContext())
                .setMessage(getString(R.string.msg_delete_item))
                .setPositiveButton(getText(R.string.ok), (dialog, which) -> {
                    deleteItem();
                    ConfigUtils.closeKeyboard(requireActivity());
                    navController.popBackStack();
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    protected abstract void deleteItem();

    protected abstract void addToReminderMenuItemClicked();

    protected abstract boolean inEditMode();
}
