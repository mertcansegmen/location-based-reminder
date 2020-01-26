package com.mertcansegmen.locationbasedreminder.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

/**
 * Broadcast Receiver that starts ReminderService on device boot.
 */
public class BootCompleteBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, ReminderService.class);
        ContextCompat.startForegroundService(context, serviceIntent);
    }
}
