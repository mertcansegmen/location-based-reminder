package com.mertcansegmen.locationbasedreminder.service;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.mertcansegmen.locationbasedreminder.repository.ReminderRepository;

import java.util.concurrent.TimeUnit;

/**
 * Broadcast receiver that starts when ReminderService is destroyed. It checks whether or not any
 * active reminders exist. If they do, that means ReminderService was destroyed by the system. In
 * that case it restarts the ReminderService.
 */
public class ServiceDestroyCheckBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ReminderRepository repository = new ReminderRepository((Application) context.getApplicationContext());

        // Wait 3 seconds for async db operations
        try { TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(repository.getActiveReminderCount() > 0) {
            Intent serviceIntent = new Intent(context, ReminderService.class);
            ContextCompat.startForegroundService(context, serviceIntent);
        }
    }
}
