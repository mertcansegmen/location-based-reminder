package com.mertcansegmen.locationbasedreminder.ui.notes;

import android.content.res.Configuration;
import android.os.Bundle;
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
import com.mertcansegmen.locationbasedreminder.model.Note;
import com.mertcansegmen.locationbasedreminder.ui.addeditnote.AddEditNoteFragment;
import com.mertcansegmen.locationbasedreminder.util.Animator;

public class NotesFragment extends Fragment {

    private LinearLayout emptyMessageLayout;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private FloatingActionButton addNoteButton;

    private Note deletedNote;
    private int deletedPosition;

    private NotesFragmentViewModel viewModel;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        setHasOptionsMenu(true);

        addNoteButton = view.findViewById(R.id.btn_add_note);
        recyclerView = view.findViewById(R.id.recycler_view);
        emptyMessageLayout = view.findViewById(R.id.empty_msg_layout);

        viewModel = ViewModelProviders.of(this).get(NotesFragmentViewModel.class);

        Animator.animateFloatingActionButton(addNoteButton);

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
        viewModel.getAllNotes().observe(this, notes -> {
            emptyMessageLayout.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
            adapter.submitList(notes);
        });
    }

    private void configureRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        }
    }

    private void setAddNoteButtonClickListener() {
        addNoteButton.setOnClickListener(v ->
            navController.navigate(R.id.action_notesFragment_to_addEditNoteFragment)
        );
    }

    private void setOnAdapterItemClickListener() {
        adapter.setOnItemClickListener(note -> navigateForEdit(note));
    }

    private void navigateForEdit(Note note) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AddEditNoteFragment.BUNDLE_KEY_NOTE, note);
        navController.navigate(R.id.action_notesFragment_to_addEditNoteFragment, bundle);
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
                deletedNote = adapter.getNoteAt(deletedPosition);

                viewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));

                Snackbar.make(viewHolder.itemView, getString(R.string.note_deleted), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.undo), v -> viewModel.insert(deletedNote))
                        .setAnchorView(addNoteButton)
                        .show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.items_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                new MaterialAlertDialogBuilder(requireContext())
                        .setMessage(getString(R.string.msg_delete_all_notes))
                        .setPositiveButton(getText(R.string.ok), (dialog, which) -> viewModel.deleteAll())
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
