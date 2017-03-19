package com.social.backendless.utils;

import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

        return Calendar.getInstance(Locale.getDefault()).getTime();
    }

    public static String convertDateToLastSeenFormat(long timeInMiliseconds) {
        //if today: last seen today at 8:04 p.m else
        // last seen Jun 20, 2016
        //Mon Jul 25 11:26:32 EDT 2016

        Calendar recipientTime = Calendar.getInstance();
        recipientTime.setTimeInMillis(timeInMiliseconds);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "MMM dd, yyyy";
        if (now.get(Calendar.DATE) == recipientTime.get(Calendar.DATE) ) {
            return "today at " + DateFormat.format(timeFormatString, recipientTime);
        } else if (now.get(Calendar.DATE) - recipientTime.get(Calendar.DATE) == 1  ){
            return "yesterday at " + DateFormat.format(timeFormatString, recipientTime);
        } else {
            return DateFormat.format(dateTimeFormatString, recipientTime).toString();
        }
    }

    /**
     * <p>Checks if two calendars represent the same day ignoring time.</p>
     * @param cal1  the first calendar, not altered, not null
     * @param cal2  the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     */
    private static boolean isSameDay(@NonNull Calendar cal1, @NonNull Calendar cal2) {
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
}
