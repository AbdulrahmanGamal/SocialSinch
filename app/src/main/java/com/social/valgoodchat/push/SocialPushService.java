package com.social.valgoodchat.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.backendless.push.BackendlessPushService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.social.backendless.bus.RxIncomingMessageBus;
import com.social.backendless.bus.RxOutgoingMessageBus;
import com.social.backendless.database.ChatBriteDataSource;
import com.social.backendless.model.ChatMessage;
import com.social.backendless.model.ChatStatus;
import com.social.backendless.utils.Constants;
import com.social.valgoodchat.MessagesActivity;
import com.social.valgoodchat.R;

/**
 * Service to process the push notification messages
 */
public class SocialPushService extends BackendlessPushService {
    private static final String TAG = "SocialPushService";

    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onRegistered(Context context, String registrationId) {
        Toast.makeText( context, "device registered" + registrationId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUnregistered( Context context, Boolean unregistered ) {
        Toast.makeText( context, "device unregistered", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMessage( Context context, Intent intent ) {
        String messageType = intent.getStringExtra(Constants.MESSAGE_TYPE_KEY);
        if (messageType != null && messageType.equals(Constants.MESSAGE_TYPE_CHAT_KEY)) {
            String message = intent.getStringExtra( "message" );
            Log.d(TAG, "Push message received. Message: " + message);
            Log.d(TAG, "OutComingBus hasObserver: " + RxOutgoingMessageBus.getInstance().hasObservers());
            Gson gson = new Gson();
            try {
                ChatMessage messageReceived = gson.fromJson(message, ChatMessage.class);
                //check if the outcomingbus is false, if so, we need to attach to it and save the message in DB
                if (!RxOutgoingMessageBus.getInstance().hasObservers()) {
                    messageReceived.setStatus(ChatStatus.RECEIVED);
                    ChatBriteDataSource.getInstance(context).addNewMessage(messageReceived);
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context
                            .NOTIFICATION_SERVICE);
                    mNotificationManager.notify(NOTIFICATION_ID, createNotification(true, messageReceived));
                } else {
                    //find out if the app is in background to know if i should show the notification or not
                }
            } catch (JsonSyntaxException ex) {
                Log.d(TAG, "PN not a valid chat message, discarded");
            }
        }
        // When returning 'true', default Backendless onMessage implementation will be executed.
        // The default implementation displays the notification in the Android Notification Center.
        // Returning false, cancels the execution of the default implementation.
        return false;
    }

    @Override
    public void onError( Context context, String message ) {
        Toast.makeText( context, message, Toast.LENGTH_SHORT).show();
    }

   private Notification createNotification(boolean makeHeadsUpNotification, ChatMessage messageReceived) {
        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentTitle(messageReceived.getSenderId())
                .setContentText(messageReceived.getTextBody());

        notificationBuilder.setCategory(Notification.CATEGORY_MESSAGE);
        if (makeHeadsUpNotification) {
            Intent push = new Intent();
            push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            push.setClass(this, MessagesActivity.class);

            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                    push, PendingIntent.FLAG_CANCEL_CURRENT);
            notificationBuilder
                    .setContentText(messageReceived.getTextBody())
                    .setFullScreenIntent(fullScreenPendingIntent, true);
        }
        return notificationBuilder.build();
    }
}
