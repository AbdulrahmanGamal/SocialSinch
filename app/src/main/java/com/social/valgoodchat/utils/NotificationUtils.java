package com.social.valgoodchat.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.util.List;
import java.util.Map;

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
            //saved the message in DB as a notification message
            ChatBriteDataSource.getInstance(context).
                    addNotificationMessage(messageReceived.getSenderId(),
                                           messageReceived.getTextBody());
            mNotificationManager.notify(NOTIFICATION_ID, NotificationUtils.getInstance().
                                                                    getNotificationBuilder(context));
        }
    }

    /**
     * Obtains the notification object
     * @param context
     * @return
     */
    private Notification getNotificationBuilder(Context context) {
        Map<String, List<String>> notifications =
                ChatBriteDataSource.getInstance(context).getNotifications();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        int totalConversations = 0;
        int totalMessages = 0;
        //if more then 1 user talking
        if (notifications.size() > 1) {
            mInboxStyle = new NotificationCompat.InboxStyle();
            for (String senderId: notifications.keySet()) {
                totalConversations++;
                List<String> messages = notifications.get(senderId);
                totalMessages += messages.size();
                UserInfo userInfo = ChatBriteDataSource.getInstance(context).getContactById(senderId);
                for (String message : messages) {
                    mInboxStyle.addLine(userInfo.getFullName() + ": " + message);
                }
            }
            Object vars[] = {totalConversations, totalMessages};
            String contentText = context.getResources().
                    getString(R.string.multiple_user_new_messages_received_label, vars);
            String contentTitle = context.getResources().
                    getString(R.string.app_name);
            mInboxStyle.setBigContentTitle(contentTitle);
            mInboxStyle.setSummaryText(contentText);

            Bitmap largeIcon = BitmapFactory.
                    decodeResource(context.getResources(), R.drawable.ic_launcher);
            transformNotificationbuilder(notificationBuilder, largeIcon, contentTitle, contentText);
            makeHeadsUpNotification(context, null, contentText, true, notificationBuilder);
        } else {
            //just 1 person talking
            for (String senderId : notifications.keySet()) {
                UserInfo senderInfo = ChatBriteDataSource.getInstance(context).getContactById(senderId);
                Bitmap roundedBitmap =
                        ImageLoading.getPictureForNotification(context, senderInfo.getProfilePicture());
                List<String> message = notifications.get(senderId);
                mInboxStyle = new NotificationCompat.InboxStyle();
                if (message.size() == 1) {
                    mInboxStyle.addLine(message.get(0));

                    transformNotificationbuilder(notificationBuilder, roundedBitmap,
                                                 senderInfo.getFullName(), message.get(0));
                    makeHeadsUpNotification(context, senderInfo.getObjectId(),
                            message.get(0), false, notificationBuilder);
                } else {
                    String msjCounterText = context.getResources().getQuantityString(
                            R.plurals.single_user_new_messages_received_label,
                            message.size(),
                            message.size());
                    mInboxStyle.setBigContentTitle(senderInfo.getFullName());
                    for (String messageReceived : message) {
                        mInboxStyle.addLine(messageReceived);
                    }
                    mInboxStyle.setSummaryText(msjCounterText);

                    transformNotificationbuilder(notificationBuilder, roundedBitmap,
                                                 senderInfo.getFullName(), msjCounterText);
                    makeHeadsUpNotification(context, senderInfo.getObjectId(),
                            msjCounterText, false, notificationBuilder);
                }
            }
        }

        notificationBuilder.setCategory(Notification.CATEGORY_MESSAGE);
        notificationBuilder.setStyle(mInboxStyle);
        return notificationBuilder.build();
    }

    private void transformNotificationbuilder(NotificationCompat.Builder notificationBuilder,
                                              Bitmap largeIcon,
                                              String contentTitle,
                                              String contentText) {
        notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(largeIcon)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setLights(Color.GREEN, 1000, 5000)
                .setDefaults(Notification.DEFAULT_VIBRATE |
                        Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                .setAutoCancel(true);
    }
    /**
     * Adds capability of heads up for 1 conversation
     * @param context
     * @param recipientId
     * @param message
     * @param isMultiple
     * @param notificationBuilder
     */
    private void makeHeadsUpNotification(Context context,
                                         String recipientId,
                                         String message,
                                         boolean isMultiple,
                                         NotificationCompat.Builder notificationBuilder) {
        Intent push = new Intent();
        push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isMultiple) {
            push.setClass(context, TabActivity.class);
        } else {
            push.putExtra(Constants.RECIPIENT_ID, recipientId);
            push.setClass(context, MessagesActivity.class);
        }

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, push, PendingIntent.FLAG_CANCEL_CURRENT);
        notificationBuilder
                .setContentText(message)
                .setContentIntent(pendingIntent);
    }
}