package com.mertcansegmen.locationbasedreminder.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DevicePrefs {

    private DevicePrefs() {
    }

    public static void setPrefs(Context context, String key, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getPrefs(Context context, String key, int defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, defaultValue);
    }
}