package com.mertcansegmen.locationbasedreminder.ui.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Note;
import com.mertcansegmen.locationbasedreminder.ui.ListingFragment;
import com.mertcansegmen.locationbasedreminder.ui.addeditnote.AddEditNoteFragment;
import com.mertcansegmen.locationbasedreminder.util.Animator;

public class NotesFragment extends ListingFragment {

    private NoteAdapter adapter;
    private FloatingActionButton addNoteButton;

    private NotesFragmentViewModel viewModel;

    @Override
    protected View inflateFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addNoteButton = view.findViewById(R.id.btn_add_note);
        Animator.animateFloatingActionButton(addNoteButton);

        initViewModel();
        initObserver();
        setAddButtonClickListener();
        setAdapterItemClickListener();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(NotesFragmentViewModel.class);
    }

    private void initObserver() {
        viewModel.getAllNotes().observe(this, notes -> {
            emptyMessageLayout.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
            adapter.submitList(notes);
        });
    }

    private void setAddButtonClickListener() {
        addNoteButton.setOnClickListener(v ->
                navController.navigate(R.id.action_notesFragment_to_addEditNoteFragment)
        );
    }

    private void setAdapterItemClickListener() {
        adapter.setOnItemClickListener(note -> navigateForEdit(note));
    }

    private void navigateForEdit(Note note) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AddEditNoteFragment.BUNDLE_KEY_NOTE, note);
        navController.navigate(R.id.action_notesFragment_to_addEditNoteFragment, bundle);
    }

    @Override
    protected void initAdapter() {
        adapter = new NoteAdapter();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onAdapterItemSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int deletedPosition = viewHolder.getAdapterPosition();
        Note deletedNote = adapter.getNoteAt(deletedPosition);

        viewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));

        Snackbar.make(viewHolder.itemView, getString(R.string.note_deleted), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.undo), v -> viewModel.insert(deletedNote))
                .setAnchorView(addNoteButton)
                .show();
    }

    @Override
    protected void onDeleteAllOptionSelected() {
        new MaterialAlertDialogBuilder(requireContext())
                .setMessage(getString(R.string.msg_delete_all_notes))
                .setPositiveButton(getText(R.string.ok), (dialog, which) -> viewModel.deleteAll())
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }
}
