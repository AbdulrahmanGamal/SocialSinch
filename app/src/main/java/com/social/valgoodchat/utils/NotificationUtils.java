package com.social.valgoodchat.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.social.backendless.database.ChatBriteDataSource;
import com.social.backendless.model.ChatMessage;
import com.social.backendless.model.UserInfo;
import com.social.valgoodchat.MessagesActivity;
import com.social.valgoodchat.R;
import com.social.valgoodchat.TabActivity;
import com.social.valgoodchat.app.SocialSinchApplication;

import static android.content.Context.MODE_PRIVATE;

/**
 * Encapsulates all the process of showing an offline message and showing a notification.
 */
public class NotificationUtils {

    private static NotificationUtils sNotificationUtils;
    private NotificationCompat.InboxStyle mInboxStyle;

    private static final int NOTIFICATION_ID = 1;

    private static final String TAG = "NotificationUtils";

    public static NotificationUtils getInstance() {
        if (sNotificationUtils == null) {
            sNotificationUtils = new NotificationUtils();
        }
        return sNotificationUtils;
    }

    /**
     * Verify is no activity is open to display a notification
     * @param context
     * @param chatMessage
     */
    public void showNotificationIfNeeded(Context context, String chatMessage) {
        //if there's no activity visible, show the notification
        if (!SocialSinchApplication.isActivityVisible()) {
            ChatMessage messageReceived = new Gson().fromJson(chatMessage, ChatMessage.class);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            UserInfo senderInfo = ChatBriteDataSource.getInstance(context).
                    getContactById(messageReceived.getSenderId());
            //get the global notification preference
            SharedPreferences sharedPreferences =
                    context.getSharedPreferences("notifications", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int msjCount = 1;
            if (sharedPreferences.getString(String.valueOf(NOTIFICATION_ID), null) != null) {
                msjCount = sharedPreferences.getInt(senderInfo.getObjectId(), 0) + 1;
                editor.putInt(senderInfo.getObjectId(), msjCount);
            } else {
                editor.putInt(senderInfo.getObjectId(), msjCount);
            }
            mNotificationManager.notify(NOTIFICATION_ID,
                    NotificationUtils.getInstance().
                            getNotificationBuilder(context, senderInfo, messageReceived, msjCount));
            editor.putString(String.valueOf(NOTIFICATION_ID), "notifications");
            editor.apply();
        }
    }

    /**
     * Obtains the notification object
     * @param context
     * @param senderInfo
     * @param messageReceived
     * @param msjCounter
     * @return
     */
    private Notification getNotificationBuilder(Context context,
                                                              UserInfo senderInfo,
                                                              ChatMessage messageReceived,
                                                              int msjCounter) {
        Bitmap roundedBitmap =
                ImageLoading.getPictureForNotification(context, senderInfo.getProfilePicture());
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(roundedBitmap)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentTitle(senderInfo.getFullName())
                .setContentText(messageReceived.getTextBody())
                .setLights(Color.GREEN, 1000, 5000)
                .setDefaults(Notification.DEFAULT_VIBRATE |
                        Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                .setAutoCancel(true);

        if (msjCounter == 1) {
            mInboxStyle = new NotificationCompat.InboxStyle();
            mInboxStyle.addLine(messageReceived.getTextBody());
            makeHeadsUpNotification(context, senderInfo.getObjectId(),
                    messageReceived.getTextBody(), notificationBuilder);
        } else {
            String msjCounterText = context.getResources().getQuantityString(
                    R.plurals.new_messages_received_label,
                    msjCounter,
                    msjCounter);
            mInboxStyle.setBigContentTitle(senderInfo.getFullName());
            mInboxStyle.addLine(messageReceived.getTextBody());
            mInboxStyle.setSummaryText(msjCounterText);
            makeHeadsUpNotification(context, senderInfo.getObjectId(),
                    msjCounterText, notificationBuilder);
        }
        notificationBuilder.setCategory(Notification.CATEGORY_MESSAGE);
        notificationBuilder.setStyle(mInboxStyle);
        return notificationBuilder.build();
    }

    /**
     * Adds capability of heads up
     * @param context
     * @param recipientId
     * @param message
     * @param notificationBuilder
     */
    private void makeHeadsUpNotification(Context context,
                                         String recipientId,
                                         String message,
                                         NotificationCompat.Builder notificationBuilder) {
        Intent push = new Intent();
        push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        push.putExtra(Constants.RECIPIENT_ID, recipientId);
        push.setClass(context, MessagesActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(TabActivity.class);
        stackBuilder.addNextIntent(push);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, push, PendingIntent.FLAG_CANCEL_CURRENT);
        notificationBuilder
                .setContentText(message)
                .setContentIntent(pendingIntent);
    }
}