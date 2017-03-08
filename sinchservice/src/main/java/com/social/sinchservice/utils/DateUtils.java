package com.social.sinchservice.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Class with utils methods to convert date to string and viceversa
 */

public class DateUtils {
    private static final String TAG = "DateUtils";

    public static String convertDateToString(@NonNull Date messageDate) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        return dateFormat.format(messageDate);
    }

    public static Date convertStringToDate(@NonNull String messageDate) {
        try {
            final SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            return dateFormat.parse(messageDate);
        } catch (final ParseException e) {
            Log.e(TAG, "Parsing exception: " + e.getMessage());
        }

        return null;
    }
}
