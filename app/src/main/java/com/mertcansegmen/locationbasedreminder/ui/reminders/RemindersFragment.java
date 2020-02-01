package com.mertcansegmen.locationbasedreminder.ui.reminders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.ReminderWithNotePlacePlaceGroup;
import com.mertcansegmen.locationbasedreminder.ui.ListingFragment;
import com.mertcansegmen.locationbasedreminder.ui.addeditreminder.AddEditReminderFragment;
import com.mertcansegmen.locationbasedreminder.viewmodel.RemindersFragmentViewModel;

public class RemindersFragment extends ListingFragment {

    private ReminderAdapter adapter;

    private RemindersFragmentViewModel viewModel;

    @Override
    protected View inflateFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reminders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAddButtonClickListener();
        setAdapterItemClickListener();
    }

    @Override
    protected void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(RemindersFragmentViewModel.class);
    }

    @Override
    protected void initListObserver() {
        viewModel.getAllReminders().observe(this, reminders -> {
            emptyMessageLayout.setVisibility(reminders.isEmpty() ? View.VISIBLE : View.GONE);
            adapter.submitList(reminders);

            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setAddButtonClickListener() {
        fab.setOnClickListener(v ->
                navController.navigate(R.id.action_remindersFragment_to_addEditReminderFragment)
        );
    }

    private void setAdapterItemClickListener() {
        adapter.setOnItemClickListener(reminder -> navigateForEdit(reminder));
    }

    private void navigateForEdit(ReminderWithNotePlacePlaceGroup reminder) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AddEditReminderFragment.BUNDLE_KEY_REMINDER, reminder);
        navController.navigate(R.id.action_remindersFragment_to_addEditReminderFragment, bundle);
    }

    @Override
    protected void initAdapter() {
        adapter = new ReminderAdapter();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onAdapterItemSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int deletedPosition = viewHolder.getAdapterPosition();
        ReminderWithNotePlacePlaceGroup deletedReminder = adapter.getReminderAt(deletedPosition);

        viewModel.delete(adapter.getReminderAt(viewHolder.getAdapterPosition()));

        Snackbar.make(viewHolder.itemView, getString(R.string.reminder_deleted), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.undo), v -> viewModel.insert(deletedReminder))
                .setAnchorView(fab)
                .show();
    }

    @Override
    protected void onDeleteAllOptionSelected() {
        new MaterialAlertDialogBuilder(requireContext())
                .setMessage(getString(R.string.msg_delete_all_reminders))
                .setPositiveButton(getText(R.string.ok), (dialog, which) -> viewModel.deleteAll())
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    @Override
    protected void navigateSettings() {
        navController.navigate(R.id.action_remindersFragment_to_settingsFragment);
    }
}