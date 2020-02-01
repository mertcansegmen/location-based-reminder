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
import android.os.Looper;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.mertcansegmen.locationbasedreminder.App.CHANNEL_ID;

public class ReminderService extends Service implements LocationListener {

    public static final String REMINDER_SERVICE_DESTROYED = "com.mertcansegmen.locationbasedreminder.REMINDER_SERVICE_DESTROYED";

    public static final String ACTION_RESET = "com.mertcansegmen.locationbasedreminder.ACTION_RESET";
    public static final String EXTRA_REMINDER_ID = "com.mertcansegmen.locationbasedreminder.EXTRA_REMINDER_ID";

    private static final int REQUEST_CODE_MAIN_ACTIVITY = 999999;

    private static final int NOTIFICATION_ID_FOREGROUND = 999999;

    private List<ReminderWithNotePlacePlaceGroup> reminders;

    private ReminderRepository repository;

    private LocationManager locationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        repository = new ReminderRepository(getApplication());

        startForeground(NOTIFICATION_ID_FOREGROUND, createForegroundNotification());

        initObserver();
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent(REMINDER_SERVICE_DESTROYED);
        sendBroadcast(intent);
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

    private void initObserver() {
        repository.getActiveReminders().observeForever(reminders -> {
            // If there is no active reminder, stop service
            if (reminders == null || reminders.isEmpty()) {
                stopSelf();
            }

            this.reminders = reminders;
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startLocationListenerWithTimeOut();

        return START_STICKY;
    }

    /**
     * Starts location listener after 3 seconds. When location listener starts, it checks for active
     * reminders in database. If listener starts immediately, it won't see the previously set
     * reminder since db operations are being executed asynchronously.
     */
    private void startLocationListenerWithTimeOut() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (reminders != null && !reminders.isEmpty()) {
                startLocationListener();
            }
        });
    }

    private void startLocationListener() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), R.string.grant_location_permission, Toast.LENGTH_SHORT).show();
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this, Looper.getMainLooper());
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, this, Looper.getMainLooper());
    }

    @Override
    public void onLocationChanged(Location currentLocation) {
//        testLocationUpdates();
        if (reminders != null && !reminders.isEmpty()) {
            for (ReminderWithNotePlacePlaceGroup reminder : reminders) {
                // If reminder has no place or place group, inform user and deactivate reminder
                if (reminder.getPlace() == null && reminder.getPlaceGroupWithPlaces() == null) {
                    Toast.makeText(this,
                            getString(R.string.reminder_deactivated_place_removed),
                            Toast.LENGTH_LONG).show();
                    repository.setActive(reminder, false);
                    continue;
                }

                // Reminder set to a place
                if (reminder.getPlace() != null) {
                    showReminderNotificationIfArrived(reminder, reminder.getPlace(), currentLocation);
                }
                // Reminder set to a place group
                if (reminder.getPlaceGroupWithPlaces() != null) {
                    // If place group doesn't contain any place, inform user and deactivate reminder
                    if (reminder.getPlaceGroupWithPlaces().getPlaces().isEmpty()) {
                        Toast.makeText(this,
                                getString(R.string.reminder_deactivated_empty_place_group),
                                Toast.LENGTH_LONG).show();
                        repository.setActive(reminder, false);
                        continue;
                    }

                    // Check for every place in place group
                    for (Place place : reminder.getPlaceGroupWithPlaces().getPlaces()) {
                        showReminderNotificationIfArrived(reminder, place, currentLocation);
                    }
                }
            }
        } else {
            locationManager.removeUpdates(this);
            stopSelf();
        }
    }

    private void showReminderNotificationIfArrived(ReminderWithNotePlacePlaceGroup reminder,
                                                   Place reminderPlace, Location currentLocation) {
        // Turn place object into location object
        Location reminderLocation = placeToLocation(reminderPlace);

        // Check if current location is inside the radius of the reminders place
        if (currentLocation.distanceTo(reminderLocation) < reminderPlace.getRadius()) {
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
        Intent resetReminderIntent = new Intent(this, NotificationActionsBroadcastReceiver.class);
        resetReminderIntent.setAction(ACTION_RESET);
        resetReminderIntent.putExtra(EXTRA_REMINDER_ID, reminder.getReminder().getReminderId());
        return PendingIntent.getBroadcast(this, reminder.getReminder().getReminderId().intValue(), resetReminderIntent, 0);
    }

    private PendingIntent createMainActivityPendingIntent() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        return PendingIntent.getActivity(this, REQUEST_CODE_MAIN_ACTIVITY, mainActivityIntent, 0);
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    int i;
//    Toast t;
//
//    private void testLocationUpdates() {
//        if(t != null) t.cancel();
//        t = Toast.makeText(this, "" + i, Toast.LENGTH_SHORT);
//        t.show();
//        i++;
//    }
}