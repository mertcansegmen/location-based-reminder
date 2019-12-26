package com.mertcansegmen.locationbasedreminder.ui.places;

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

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.ui.addeditplace.AddEditPlaceFragment;
import com.mertcansegmen.locationbasedreminder.ui.addeditplacegroup.AddEditPlaceGroupFragment;
import com.mertcansegmen.locationbasedreminder.ui.notes.NoteAdapter;
import com.mertcansegmen.locationbasedreminder.util.Animator;
import com.mertcansegmen.locationbasedreminder.util.PlaceChip;

public class PlacesFragment extends Fragment {

    private LinearLayout emptyMessageLayout;
    private FloatingActionButton addPlaceButton;
    private RecyclerView recyclerView;
    private PlaceAdapter adapter;

    private Place deletedPlace;
    private int deletedPosition;

    private PlacesFragmentViewModel viewModel;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_places, container, false);
        setHasOptionsMenu(true);

        addPlaceButton = view.findViewById(R.id.btn_add_place);
        recyclerView = view.findViewById(R.id.recycler_view);
        emptyMessageLayout = view.findViewById(R.id.empty_msg_layout);

        viewModel = ViewModelProviders.of(this).get(PlacesFragmentViewModel.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new PlaceAdapter();
        recyclerView.setAdapter(adapter);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        }

        Animator.animateFloatingActionButton(addPlaceButton);

        viewModel.getAllPlaces().observe(this, places -> {
            emptyMessageLayout.setVisibility(places.isEmpty() ? View.VISIBLE : View.GONE);

            adapter.submitList(places);
        });

        addPlaceButton.setOnClickListener(v -> {
            navController.navigate(R.id.action_placesFragment_to_addEditPlaceFragment);
        });

        adapter.setOnItemClickListener(place -> navigateForEdit(place));

        // This is needed for recycler view to go top when new record is added. Without this, new
        // element is getting inserted off of the screen.
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                if(positionStart == 0) {
                    recyclerView.scrollToPosition(0);
                }
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
                deletedPlace = adapter.getPlaceAt(deletedPosition);

                viewModel.delete(adapter.getPlaceAt(viewHolder.getAdapterPosition()));

                Snackbar.make(viewHolder.itemView, "Place deleted.", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", v -> viewModel.insert(deletedPlace))
                        .setAnchorView(addPlaceButton)
                        .show();
            }
        }).attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    private void navigateForEdit(Place place) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AddEditPlaceFragment.BUNDLE_KEY_PLACE, place);
        navController.navigate(R.id.action_placesFragment_to_addEditPlaceFragment, bundle);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.places_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_places:
                new MaterialAlertDialogBuilder(requireContext())
                        .setMessage(getString(R.string.question_delete_all_places))
                        .setPositiveButton(getText(R.string.ok), (dialog, which) -> {
                            viewModel.deleteAll();
                        })
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
