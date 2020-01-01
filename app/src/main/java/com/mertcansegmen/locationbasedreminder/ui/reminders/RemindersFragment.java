package com.mertcansegmen.locationbasedreminder.ui.reminders;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.ReminderWithNotePlacePlaceGroup;
import com.mertcansegmen.locationbasedreminder.util.Animator;

public class RemindersFragment extends Fragment {

    private LinearLayout emptyMessageLayout;
    private FloatingActionButton addReminderButton;
    private RecyclerView recyclerView;
    private ReminderAdapter adapter;

    private ReminderWithNotePlacePlaceGroup deletedReminder;
    private int deletedPosition;

    private RemindersFragmentViewModel viewModel;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminders, container, false);
        setHasOptionsMenu(true);

        addReminderButton = view.findViewById(R.id.btn_add_reminder);
        recyclerView = view.findViewById(R.id.recycler_view);
        emptyMessageLayout = view.findViewById(R.id.empty_msg_layout);

        viewModel = ViewModelProviders.of(this).get(RemindersFragmentViewModel.class);

        Animator.animateFloatingActionButton(addReminderButton);

        setObserver();
        configureRecyclerView();
        setAddNoteButtonClickListener();
        setOnAdapterItemClickListener();
        registerAdapterDataObserver();
        setItemTouchHelper();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    private void setObserver() {
        viewModel.getAllReminders().observe(this, reminders -> {
            emptyMessageLayout.setVisibility(reminders.isEmpty() ? View.VISIBLE : View.GONE);
            adapter.submitList(reminders);
            Log.i("Mert", "setObserver: " + reminders);
        });
    }

    private void configureRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new ReminderAdapter();
        recyclerView.setAdapter(adapter);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        }
    }

    private void setAddNoteButtonClickListener() {
//        addReminderButton.setOnClickListener(v ->
//                navController.navigate(R.id.action_remindersFragment_to_addEditReminderFragment)
//        );
    }

    private void setOnAdapterItemClickListener() {
        adapter.setOnItemClickListener(reminder -> navigateForEdit(reminder));
    }

    private void navigateForEdit(ReminderWithNotePlacePlaceGroup reminder) {
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(AddEditReminderFragment.BUNDLE_KEY_REMINDER, reminder);
//        navController.navigate(R.id.action_remindersFragment_to_addEditReminderFragment, bundle);
    }

    /**
     * This is needed for recycler view to go top when new record is added. Without this, new
     * element is getting inserted off of the screen.
     */
    private void registerAdapterDataObserver() {
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                if(positionStart == 0) recyclerView.scrollToPosition(0);
            }
        });
    }

    private void setItemTouchHelper() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deletedPosition = viewHolder.getAdapterPosition();
                deletedReminder = adapter.getReminderAt(deletedPosition);

                viewModel.delete(adapter.getReminderAt(viewHolder.getAdapterPosition()));

                Snackbar.make(viewHolder.itemView, getString(R.string.note_deleted), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.undo), v -> viewModel.insert(deletedReminder))
                        .setAnchorView(addReminderButton)
                        .show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.reminders_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_reminders:
                new MaterialAlertDialogBuilder(requireContext())
                        .setMessage(getString(R.string.msg_delete_all_reminders))
                        .setPositiveButton(getText(R.string.ok), (dialog, which) -> viewModel.deleteAll())
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}