package com.mertcansegmen.locationbasedreminder.ui.addeditplace;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.ui.AddEditFragment;
import com.mertcansegmen.locationbasedreminder.ui.MainActivity;
import com.mertcansegmen.locationbasedreminder.ui.addeditreminder.AddEditReminderFragment;
import com.mertcansegmen.locationbasedreminder.util.DevicePrefs;
import com.mertcansegmen.locationbasedreminder.viewmodel.AddEditPlaceFragmentViewModel;

import timber.log.Timber;

public class AddEditPlaceFragment extends AddEditFragment implements OnMapReadyCallback, LocationListener {

    public static final String BUNDLE_KEY_PLACE = "com.mertcansegmen.locationbasedreminder.BUNDLE_KEY_PLACE";
    private static final String BUNDLE_KEY_MAP_VIEW = "com.mertcansegmen.locationbasedreminder.BUNDLE_KEY_MAP_VIEW";
    public static final String PREF_KEY_DEFAULT_RANGE = "com.mertcansegmen.locationbasedreminder.PREF_KEY_DEFAULT_RANGE";
    public static final int DEFAULT_RANGE = 100;
    private static final float DEFAULT_ZOOM = 15F;

    private TextView radiusTextView;
    private AppCompatSeekBar radiusSeekBar;

    private Place currentPlace;

    private MapView mapView;
    private GoogleMap googleMap;
    private Marker currentPlaceMarker;
    private LocationManager locationManager;
    private Circle radiusCircle;

    private AddEditPlaceFragmentViewModel viewModel;

    @Override
    protected View inflateFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_edit_place, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initViewModel();
        retrievePlace();

        if (isGoogleServicesAvailable()) {
            initMap(savedInstanceState);
        } else {
            Toast.makeText(requireContext(), getString(R.string.google_services_not_available), Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews(View view) {
        mapView = view.findViewById(R.id.map_view);
        radiusSeekBar = view.findViewById(R.id.seekbar_radius);
        radiusTextView = view.findViewById(R.id.txt_radius);
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(AddEditPlaceFragmentViewModel.class);
    }

    private void retrievePlace() {
        if (getArguments() != null && getArguments().getParcelable(BUNDLE_KEY_PLACE) != null) {
            currentPlace = getArguments().getParcelable(BUNDLE_KEY_PLACE);
            ((MainActivity) requireActivity()).getSupportActionBar().setTitle(currentPlace.getName());
        }
    }

    private void initMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(BUNDLE_KEY_MAP_VIEW);
        }

        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);
    }

    private boolean isGoogleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(requireContext());
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
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

        startLocationListener();

        if (isFirstMapStart()) {
            // Map started for the first time
            if (inEditMode()) {
                addMarker(currentPlace);
                drawCircle(currentPlace.getRadius(),
                        new LatLng(currentPlace.getLatitude(), currentPlace.getLongitude()));
                goToLocation(currentPlace.getLatitude(), currentPlace.getLongitude(), DEFAULT_ZOOM);
                viewModel.setRadius(currentPlace.getRadius());
            } else {
                viewModel.setRadius(DevicePrefs.getPrefs(requireContext(), PREF_KEY_DEFAULT_RANGE, DEFAULT_RANGE));
            }
        } else {
            // Map restarted due to configuration change such as screen rotation, language change etc..
            if (inEditMode()) {
                addMarker(currentPlace);
                drawCircle(currentPlace.getRadius(),
                        new LatLng(currentPlace.getLatitude(), currentPlace.getLongitude()));
            }
            goToLocation(viewModel.getLastKnownScreenLocation().latitude,
                    viewModel.getLastKnownScreenLocation().longitude, viewModel.getLastKnownZoom());
        }

        radiusSeekBar.setProgress(viewModel.getRadius());
        radiusTextView.setText(getString(R.string.radius_text, viewModel.getRadius()));
        radiusCircle = drawCircle(viewModel.getRadius(), googleMap.getCameraPosition().target);

        setOnCameraMoveListener();
        setSeekBarListener();
    }

    private void setOnCameraMoveListener() {
        googleMap.setOnCameraMoveListener(() -> {
            radiusCircle = drawCircle(viewModel.getRadius(), googleMap.getCameraPosition().target);
            viewModel.setLastKnownScreenLocation(googleMap.getCameraPosition().target);
            viewModel.setLastKnownZoom(googleMap.getCameraPosition().zoom);
        });
    }

    private void setSeekBarListener() {
        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                viewModel.setRadius(progress);
                radiusTextView.setText(getString(R.string.radius_text, viewModel.getRadius()));
                radiusCircle = drawCircle(viewModel.getRadius(), googleMap.getCameraPosition().target);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void addMarker(Place place) {
        if (currentPlaceMarker != null) currentPlaceMarker.remove();

        MarkerOptions options = new MarkerOptions()
                .title(place.getName())
                .icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_icon))
                .snippet(getString(R.string.marker_snippet, place.getRadius()))
                .position(new LatLng(place.getLatitude(), place.getLongitude()));
        currentPlaceMarker = googleMap.addMarker(options);
        configureMarkerSnippet();
    }

    /**
     * Marker snippet normally can't be more than 1 line, this makes it possible. Must add line
     * break to snippet text when setting it.
     */
    private void configureMarkerSnippet() {
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LinearLayout info = new LinearLayout(requireContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(requireContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(requireContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
    }

    /**
     * Converts vector image to bitmap.
     */
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private Circle drawCircle(int radius, LatLng latLng) {
        if (radiusCircle != null) removeCircle();

        CircleOptions options = new CircleOptions()
                .center(latLng)
                .radius(radius)
                .fillColor(getResources().getColor(R.color.colorRadiusFill))
                .strokeColor(getResources().getColor(R.color.colorStroke))
                .strokeWidth(2);
        return googleMap.addCircle(options);
    }

    private void removeCircle() {
        radiusCircle.remove();
        radiusCircle = null;
    }

    /**
     * @return true if map started for the first time, false otherwise
     */
    private boolean isFirstMapStart() {
        return viewModel.getLastKnownScreenLocation() == null && viewModel.getLastKnownZoom() == null;
    }

    private void goToLocation(double lat, double lng, float zoom) {
        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        googleMap.moveCamera(update);
    }

    private void startLocationListener() {
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), R.string.grant_location_permission, Toast.LENGTH_SHORT).show();
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, this);
    }

    @Override
    protected void saveMenuItemClicked() {
        navigateToNamePlaceDialog();
    }

    @Override
    protected void addToReminderMenuItemClicked() {
        addToReminder();
    }

    @Override
    protected void deleteItem() {
        deleteCurrentPlace();
    }

    private void navigateToNamePlaceDialog() {
        prepareCurrentPlace();

        Bundle bundle = new Bundle();
        bundle.putParcelable(NamePlaceDialog.BUNDLE_KEY_PLACE, currentPlace);

        navController.navigate(R.id.action_addEditPlaceFragment_to_namePlaceDialog, bundle);
    }

    private void prepareCurrentPlace() {
        if (!inEditMode()) {
            currentPlace = new Place();
        }
        LatLng latLng = googleMap.getCameraPosition().target;
        currentPlace.setLatitude(latLng.latitude);
        currentPlace.setLongitude(latLng.longitude);
        currentPlace.setRadius(viewModel.getRadius());
    }

    private void deleteCurrentPlace() {
        viewModel.delete(currentPlace);
    }

    private void addToReminder() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AddEditReminderFragment.BUNDLE_KEY_PLACE_RETRIEVED, currentPlace);
        navController.navigate(R.id.action_addEditPlaceFragment_to_addEditReminderFragment, bundle);
    }

    @Override
    protected boolean inEditMode() {
        return currentPlace != null;
    }

    @Override
    public void onLocationChanged(Location location) {
        Timber.i("x");
        if (location != null && isFirstMapStart() && !inEditMode()) {
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
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}