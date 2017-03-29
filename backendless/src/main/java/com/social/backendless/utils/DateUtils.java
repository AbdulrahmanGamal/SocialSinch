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

    public static String convertDateToLastSeenFormat(String lastTimeSeen) {
        //if today: last seen today at 8:04 p.m else
        // last seen Jun 20, 2016
        //Mon Jul 25 11:26:32 EDT 2016

        Calendar recipientTime = Calendar.getInstance();
        Date dateLastTimeSeen = convertStringToDate(lastTimeSeen);
        recipientTime.setTime(dateLastTimeSeen);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "MMM dd, yyyy";
        if (now.get(Calendar.DATE) == recipientTime.get(Calendar.DATE) ) {
            return " today at " + DateFormat.format(timeFormatString, recipientTime);
        } else if (now.get(Calendar.DATE) - recipientTime.get(Calendar.DATE) == 1  ){
            return " yesterday at " + DateFormat.format(timeFormatString, recipientTime);
        } else {
            return DateFormat.format(dateTimeFormatString, recipientTime).toString();
        }
    }

    /**
     * <p>Checks if two dates represent the same day ignoring time.</p>
     * @param dateToCompare  the first calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     */
    public static boolean isSameDay(@NonNull String dateToCompare) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Date dateParsed = convertStringToDate(dateToCompare);
        cal2.setTime(dateParsed);
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static String convertChatDate(Date timestamp) {
        Log.e(TAG, "Time to convert: " + timestamp);
        final String timeChatFormatString = "h:mm aa";
        return DateFormat.format(timeChatFormatString, timestamp).toString();
    }
}
