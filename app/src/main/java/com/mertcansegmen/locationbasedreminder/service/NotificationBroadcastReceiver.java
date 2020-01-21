package com.mertcansegmen.locationbasedreminder.service;

import android.app.Application;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mertcansegmen.locationbasedreminder.repository.ReminderRepository;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long reminderId = intent.getLongExtra(ReminderService.EXTRA_REMINDER_ID, -1);

        switch (intent.getAction()) {
            case ReminderService.ACTION_RESET:
                ReminderRepository repository = new ReminderRepository((Application) context.getApplicationContext());
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
