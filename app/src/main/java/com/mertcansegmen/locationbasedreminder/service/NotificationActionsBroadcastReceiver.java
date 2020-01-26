package com.mertcansegmen.locationbasedreminder.service;

import android.app.Application;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mertcansegmen.locationbasedreminder.repository.ReminderRepository;

/**
 * Broadcast Receiver that receives notification button clicks.
 */
public class NotificationActionsBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ReminderRepository repository = new ReminderRepository((Application) context.getApplicationContext());
        long reminderId = intent.getLongExtra(ReminderService.EXTRA_REMINDER_ID, -1);

        switch (intent.getAction()) {
            case ReminderService.ACTION_RESET:
                repository.setActive(reminderId, true);
                cancelNotification(context, reminderId);
                break;
        }
    }

    private void cancelNotification(Context context, long reminderId) {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel((int) reminderId);
    }
}
