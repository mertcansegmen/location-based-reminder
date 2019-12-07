package com.mertcansegmen.locationbasedreminder.ui.addeditplace;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.ui.MainActivity;
import com.mertcansegmen.locationbasedreminder.util.DevicePrefs;

public class AddEditPlaceFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private static final String PREF_KEY_RADIUS = "com.mertcansegmen.locationbasedreminder.PREF_KEY_RADIUS";
    private static final int DEFAULT_RADIUS = 100;
    public static final String BUNDLE_KEY_PLACE = "com.mertcansegmen.locationbasedreminder.BUNDLE_KEY_PLACE";
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final float DEFAULT_ZOOM = 15F;

    private Place currentPlace;

    private MapView mapView;
    private GoogleMap googleMap;
    private LocationManager locationManager;

    private TextView radiusTextView;
    private AppCompatSeekBar radiusSeekBar;
    private Circle radiusCircle;
    private int radius;

    private AddEditPlaceFragmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_place, container, false);
        setHasOptionsMenu(true);

        mapView = view.findViewById(R.id.map_view);
        radiusSeekBar = view.findViewById(R.id.seekbar_radius);
        radiusTextView = view.findViewById(R.id.txt_radius);

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
            currentPlace = getArguments().getParcelable(BUNDLE_KEY_PLACE);
            ((MainActivity)requireActivity()).getSupportActionBar().setTitle(currentPlace.getName());
        }
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
        googleMap.setMyLocationEnabled(true);

        configureLocationListener();

        if(currentPlace != null){
            goToLocation(currentPlace.getLatitude(), currentPlace.getLongitude());
            radius = currentPlace.getRadius();
        } else {
            radius = DevicePrefs.getPrefs(requireContext(), PREF_KEY_RADIUS, DEFAULT_RADIUS);
        }

        radiusSeekBar.setProgress(radius);
        radiusTextView.setText(getString(R.string.radius_text, radius));
        drawCircle(radius);
        this.googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                drawCircle(radius);
            }
        });
        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radius = progress;
                radiusTextView.setText(getString(R.string.radius_text, radius));
                drawCircle(radius);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

    }

    private void drawCircle(int radius) {
        if(radiusCircle != null) {
            removeCircle();
        }
        CircleOptions options = new CircleOptions()
                .center(googleMap.getCameraPosition().target)
                .radius(radius)
                .fillColor(getResources().getColor(R.color.colorRadiusFill))
                .strokeColor(getResources().getColor(R.color.colorStroke))
                .strokeWidth(2);
        radiusCircle = googleMap.addCircle(options);
    }

    private void removeCircle() {
        radiusCircle.remove();
        radiusCircle = null;
    }

    private void goToLocation(double lat, double lng) {
        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM);
        googleMap.moveCamera(update);
    }

    private void configureLocationListener() {
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), R.string.grant_location_permission, Toast.LENGTH_SHORT).show();
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
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
                configurePlaceLatLngRad();
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

    private void configurePlaceLatLngRad() {
        if(currentPlace == null) {
            currentPlace = new Place();
        }
        LatLng latLng = googleMap.getCameraPosition().target;
        currentPlace.setLatitude(latLng.latitude);
        currentPlace.setLongitude(latLng.longitude);
        currentPlace.setRadius(radius);
    }

    private void showNamePlaceDialog() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(NamePlaceDialog.BUNDLE_KEY_PLACE, currentPlace);

        DialogFragment dialog = new NamePlaceDialog();
        dialog.setCancelable(false);
        dialog.setArguments(bundle);
        dialog.show(requireActivity().getSupportFragmentManager(), "Name Place");
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null && currentPlace == null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM);
            googleMap.animateCamera(cameraUpdate);
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
}
