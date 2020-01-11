package com.mertcansegmen.locationbasedreminder.ui.services;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.model.ReminderWithNotePlacePlaceGroup;
import com.mertcansegmen.locationbasedreminder.repository.ReminderRepository;
import com.mertcansegmen.locationbasedreminder.ui.MainActivity;

import java.util.List;

import static com.mertcansegmen.locationbasedreminder.ui.App.CHANNEL_ID;

public class ReminderService extends Service implements LocationListener {

    private List<ReminderWithNotePlacePlaceGroup> reminders;

    private ReminderRepository repository;

    private LocationManager locationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        repository = new ReminderRepository(getApplication());

        startForeground(999999, createForegroundNotification());

        setObserver();
    }

    private Notification createForegroundNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText(getString(R.string.location_reminder_on))
                .setShowWhen(false)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_reminders)
                .build();
    }

    private void setObserver() {
        repository.getActiveReminders().observeForever(reminders -> {
            // If there is no active reminder, stop service
            if (reminders == null || reminders.isEmpty()) stopSelf();

            this.reminders = reminders;
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startLocationListener();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, getString(R.string.no_reminder_enabled), Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startLocationListener() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), R.string.grant_location_permission, Toast.LENGTH_SHORT).show();
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(reminders != null && !reminders.isEmpty()) {
            for(ReminderWithNotePlacePlaceGroup reminder : reminders) {
                if(reminderHasNoPlaceOrPlaceGroup(reminder)) continue;

                // Reminder set to a place
                if(reminder.getPlace() != null) {
                    showReminderNotificationIfArrived(reminder, reminder.getPlace(), location);
                }
                // Reminder set to a place group
                if(reminder.getPlaceGroupWithPlaces() != null) {
                    // Continue if place group doesn't contain any place
                    if(reminder.getPlaceGroupWithPlaces().getPlaces().isEmpty()) continue;

                    // Check for every place in place group
                    for(Place place : reminder.getPlaceGroupWithPlaces().getPlaces()) {
                        showReminderNotificationIfArrived(reminder, place, location);
                    }
                }
            }
        }
    }

    private void showReminderNotificationIfArrived(ReminderWithNotePlacePlaceGroup reminder,
                                                   Place reminderPlace, Location currentLocation) {
        // Turn place object into location object
        Location reminderLocation = placeToLocation(reminderPlace);

        // Check if current location is inside the radius of the reminder place
        if(currentLocation.distanceTo(reminderLocation) < reminderPlace.getRadius()) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_reminders_ringing)
                    .setContentTitle(getString(R.string.youve_arrived_to, reminderPlace.getName()))
                    .setContentText(reminder.getNote().getBody())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify((int) reminder.getReminder().getReminderId(), builder.build());

            repository.setActive(reminder, false);
        }
    }

    /**
     * Turns place model object into a location object
     */
    private Location placeToLocation(Place place) {
        Location location = new Location(place.getName());
        location.setLatitude(place.getLatitude());
        location.setLongitude(place.getLongitude());
        return location;
    }

    private boolean reminderHasNoPlaceOrPlaceGroup(ReminderWithNotePlacePlaceGroup reminder) {
        return reminder.getPlace() == null && reminder.getPlaceGroupWithPlaces() == null;
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
}
