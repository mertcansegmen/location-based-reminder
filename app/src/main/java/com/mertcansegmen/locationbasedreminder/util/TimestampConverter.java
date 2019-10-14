package com.mertcansegmen.locationbasedreminder.util;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimestampConverter {
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    @TypeConverter
    public static Date fromTimestamp(String date) {
        if (date != null) {
            try {
                return df.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }
}
