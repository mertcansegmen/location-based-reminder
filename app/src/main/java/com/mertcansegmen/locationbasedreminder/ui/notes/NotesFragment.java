package com.mertcansegmen.locationbasedreminder.ui.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Note;
import com.mertcansegmen.locationbasedreminder.ui.addeditnote.AddEditNoteFragment;

import java.util.List;

public class NotesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton addNoteButton;

    private NotesFragmentViewModel viewModel;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notes, container, false);

        recyclerView = v.findViewById(R.id.recycler_view);
        viewModel = ViewModelProviders.of(this).get(NotesFragmentViewModel.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        final NoteAdapter noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);

        viewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                noteAdapter.submitList(notes);
            }
        });

        noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Note note) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(AddEditNoteFragment.NOTE_BUNDLE_KEY, note);
                navController.navigate(R.id.action_notesFragment_to_addEditNoteFragment, bundle);
            }
        });

        noteAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                recyclerView.scrollToPosition(0);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                recyclerView.scrollToPosition(0);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
                recyclerView.scrollToPosition(0);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.scrollToPosition(0);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                recyclerView.scrollToPosition(0);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                recyclerView.scrollToPosition(0);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addNoteButton = view.findViewById(R.id.btn_add_note);
        navController = Navigation.findNavController(view);

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_notesFragment_to_addEditNoteFragment);
            }
        });
    }
}
