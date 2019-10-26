package com.mertcansegmen.locationbasedreminder.ui.addeditplace;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.ui.MainActivity;

public class AddEditPlaceFragment extends Fragment implements OnMapReadyCallback {

    public static final String PLACE_BUNDLE_KEY ="com.mertcansegmen.locationbasedreminder.EXTRA_PLACE";
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private Place currentPlace;

    private MapView mapView;
    private GoogleMap googleMap;

    private AddEditPlaceFragmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_place, container, false);
        setHasOptionsMenu(true);

        mapView = view.findViewById(R.id.map_view);

        viewModel = ViewModelProviders.of(this).get(AddEditPlaceFragmentViewModel.class);

        if(isGoogleServicesAvailable()) {
            initMap(savedInstanceState);
        } else {
            Toast.makeText(requireContext(), getString(R.string.google_Services_not_available), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null) {
            currentPlace = getArguments().getParcelable(PLACE_BUNDLE_KEY);
            ((MainActivity)requireActivity()).getSupportActionBar().setTitle(currentPlace.getName());
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void initMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);
    }

    private boolean isGoogleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(requireContext());
        if(isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if(api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(requireActivity(), isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(requireContext(), getString(R.string.cannot_connect_play_services), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if(currentPlace != null) {
            inflater.inflate(R.menu.edit_place_menu, menu);
        } else {
            inflater.inflate(R.menu.add_place_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_place:
            case R.id.edit_place:
                configurePlaceLatLng();
                showNamePlaceDialog();
                requireActivity().onBackPressed();
                return true;
            case R.id.delete_place:
                new MaterialAlertDialogBuilder(requireContext())
                        .setMessage(getString(R.string.delete_place_message))
                        .setPositiveButton(getText(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletePlace();
                                requireActivity().onBackPressed();
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deletePlace() {
        viewModel.delete(currentPlace);
    }

    private void configurePlaceLatLng() {
        if(currentPlace == null) {
            currentPlace = new Place();
        }
        LatLng latLng = googleMap.getCameraPosition().target;
        currentPlace.setLatitude(latLng.latitude);
        currentPlace.setLongitude(latLng.longitude);
    }

    private void showNamePlaceDialog() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(NamePlaceDialog.EXTRA_PLACE, currentPlace);

        DialogFragment dialog = new NamePlaceDialog();
        dialog.setCancelable(false);
        dialog.setArguments(bundle);
        dialog.show(requireActivity().getSupportFragmentManager(), "Name Place");
    }
}
