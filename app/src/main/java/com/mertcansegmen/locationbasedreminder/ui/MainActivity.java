package com.mertcansegmen.locationbasedreminder.ui;

import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.service.ReminderService;

public class MainActivity extends AppCompatActivity {

    private static int REQUEST_CODE_LOCATION_PERMISSION = 1000;

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);

        configureBottomNavigation();
        checkLocationPermissions();
    }

    private void configureBottomNavigation() {
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.remindersFragment, R.id.notesFragment, R.id.placesFragment, R.id.placeGroupsFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNav, navController);

    }

    /**
     * Checks for location permissions, if all permissions are granted, starts reminder service.
     */
    private void checkLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isLocationPermissionGranted()) {
                if (isBackgroundLocationPermissionGranted()) {
                    // App can access location both in the foreground and in the background
                    startReminderService();
                } else {
                    // App can only access location in the foreground.
                    requestBackgroundLocationPermission();
                }
            } else {
                // App doesn't have access to the device's location at all. Make full request
                // for permission
                requestLocationPermission();
            }
        }
    }

    /**
     * If user denied location permissions or allowed only while using the app, shows a dialog to
     * inform user that app can't work that way and he/she has to allow location permissions for all
     * the time. When user closes the dialog checks for location permissions again.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isLocationPermissionGranted()) {
                    if (isBackgroundLocationPermissionGranted()) {
                        // App can access location both in the foreground and in the background
                        startReminderService();
                    } else {
                        // App can only access location in the foreground
                        new MaterialAlertDialogBuilder(this)
                                .setMessage(R.string.location_permission_allow_all_the_time)
                                .setPositiveButton(R.string.ok, ((dialog, which) -> checkLocationPermissions()))
                                .setOnCancelListener(dialog -> checkLocationPermissions())
                                .show();
                    }
                }
            } else {
                // User denied location permissions
                new MaterialAlertDialogBuilder(this)
                        .setMessage(R.string.location_permission_allow_location)
                        .setPositiveButton(R.string.ok, ((dialog, which) -> checkLocationPermissions()))
                        .setOnCancelListener(dialog -> checkLocationPermissions())
                        .show();
            }
        }
    }

    /**
     * @return true if location permissions are granted, false otherwise
     */
    private boolean isLocationPermissionGranted() {
        return ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Checks if permission is granted for background location updates. Returns true for phones
     * with Android 9 or lower since they don't have to grant permission for background location
     * updates, it is granted by default.
     *
     * @return true if background location permissions are granted, false otherwise
     */
    private boolean isBackgroundLocationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return ActivityCompat.checkSelfPermission(this,
                    permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    /**
     * Requests for location permissions. Includes permission to get location updates in the
     * background for phones with Android 10 or higher.
     */
    private void requestLocationPermission() {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions = new String[]{
                    permission.ACCESS_COARSE_LOCATION,
                    permission.ACCESS_FINE_LOCATION,
                    permission.ACCESS_BACKGROUND_LOCATION
            };
        } else {
            permissions = new String[]{
                    permission.ACCESS_COARSE_LOCATION,
                    permission.ACCESS_FINE_LOCATION
            };
        }
        ActivityCompat.requestPermissions(this,
                permissions, REQUEST_CODE_LOCATION_PERMISSION);
    }

    /**
     * Requests permission to get location updates in the background for phones with Android 10 or
     * higher.
     */
    private void requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            String[] permissions = {permission.ACCESS_BACKGROUND_LOCATION};
            ActivityCompat.requestPermissions(this,
                    permissions, REQUEST_CODE_LOCATION_PERMISSION);
        }
    }

    private void startReminderService() {
        Intent serviceIntent = new Intent(this, ReminderService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }
}
