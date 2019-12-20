package com.mertcansegmen.locationbasedreminder.ui.notes;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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

import java.util.List;

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

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        }

        Animator.animateFloatingActionButton(addNoteButton);

        viewModel.getAllNotes().observe(this, notes -> {
            emptyMessageLayout.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);

            adapter.submitList(notes);
        });

        adapter.setOnItemClickListener(note -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(AddEditNoteFragment.NOTE_BUNDLE_KEY, note);
            navController.navigate(R.id.action_notesFragment_to_addEditNoteFragment, bundle);
        });

        // This is needed for recycler view to go top when new record is added. Without this, new
        // element is getting inserted off of the screen.
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
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
                deletedPosition = viewHolder.getAdapterPosition();
                deletedNote = adapter.getNoteAt(deletedPosition);

                viewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));

                Snackbar.make(viewHolder.itemView, "Note deleted.", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", v -> viewModel.insertBack(deletedNote))
                        .setAnchorView(addNoteButton)
                        .show();
            }
        }).attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        addNoteButton.setOnClickListener(v -> navController.navigate(R.id.action_notesFragment_to_addEditNoteFragment));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.notes_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                new MaterialAlertDialogBuilder(requireContext())
                        .setMessage(getString(R.string.question_delete_all_notes))
                        .setPositiveButton(getText(R.string.ok), (dialog, which) -> viewModel.deleteAll())
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
