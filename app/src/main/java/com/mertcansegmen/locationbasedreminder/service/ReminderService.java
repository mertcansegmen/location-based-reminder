package com.mertcansegmen.locationbasedreminder.service;

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

import static com.mertcansegmen.locationbasedreminder.App.CHANNEL_ID;

public class ReminderService extends Service implements LocationListener {

    public static final String ACTION_RESET = "com.mertcansegmen.locationbasedreminder.ACTION_RESET";
    public static final String EXTRA_REMINDER_ID = "com.mertcansegmen.locationbasedreminder.EXTRA_REMINDER_ID";

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
        PendingIntent mainActivityPendingIntent = createMainActivityPendingIntent();

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText(getString(R.string.location_reminder_on))
                .setShowWhen(false)
                .setContentIntent(mainActivityPendingIntent)
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
        if(reminders != null && !reminders.isEmpty()) {
            startLocationListener();
        }

        return START_STICKY;
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
                // Continue if reminder has no place or place group
                if(reminder.getPlace() == null && reminder.getPlaceGroupWithPlaces() == null) continue;

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

        // Check if current location is inside the radius of the reminders place
        if(currentLocation.distanceTo(reminderLocation) < reminderPlace.getRadius()) {
            String noteTitle = reminder.getNote().getTitle();
            String noteBody = reminder.getNote().getBody();
            String reminderTitle = getString(R.string.youve_arrived_to, reminderPlace.getName());
            String reminderText = noteTitle.equals("") ? noteBody : noteTitle + ":\n" + noteBody;

            PendingIntent mainActivityPendingIntent = createMainActivityPendingIntent();
            PendingIntent resetReminderPendingIntent = createResetPendingIntent(reminder);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_reminders_ringing)
                    .setContentTitle(reminderTitle)
                    .setContentText(reminderText)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(reminderText))
                    .setContentIntent(mainActivityPendingIntent)
                    .addAction(R.drawable.ic_reminders, getString(R.string.reset), resetReminderPendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_MAX);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(reminder.getReminder().getReminderId().intValue(), builder.build());

            repository.setActive(reminder, false);
        }
    }

    private PendingIntent createResetPendingIntent(ReminderWithNotePlacePlaceGroup reminder) {
        Intent resetReminderIntent = new Intent(this, NotificationBroadcastReceiver.class);
        resetReminderIntent.setAction(ACTION_RESET);
        resetReminderIntent.putExtra(EXTRA_REMINDER_ID, reminder.getReminder().getReminderId());
        return PendingIntent.getBroadcast(this, reminder.getReminder().getReminderId().intValue(), resetReminderIntent, 0);
    }

    private PendingIntent createMainActivityPendingIntent() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        return PendingIntent.getActivity(this,999999, mainActivityIntent, 0);
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