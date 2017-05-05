package com.social.valgoodchat.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.backendless.push.BackendlessPushService;
import com.social.backendless.PublishSubscribeHandler;
import com.social.backendless.bus.RxOutgoingMessageBus;
import com.social.backendless.model.ChatMessage;
import com.social.backendless.utils.Constants;
import com.social.backendless.utils.LoggedUser;
import com.social.valgoodchat.MessagesActivity;
import com.social.valgoodchat.R;
import com.social.valgoodchat.app.SocialSinchApplication;

import io.reactivex.disposables.Disposable;

/**
 * Service to process the push notification messages
 */
public class SocialPushService extends BackendlessPushService {
    private static final String TAG = "SocialPushService";
    private Disposable mDisposable;
    private static PublishSubscribeHandler mSubscriberHandler;

    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onRegistered(Context context, String registrationId) {
        Log.d(TAG, "Device registered " + registrationId);
        initSubscriber(context);
    }

    private void initSubscriber(Context context) {
        mDisposable = RxOutgoingMessageBus.getInstance().getMessageObservable().subscribe(chatMessage -> {
            //if there's no activity visible, show the notification
            if (!SocialSinchApplication.isActivityVisible()) {
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(NOTIFICATION_ID, createNotification(true, chatMessage));
            }
        });
        mSubscriberHandler = new PublishSubscribeHandler(context);
    }
    @Override
    public void onUnregistered( Context context, Boolean unregistered ) {
        Log.d(TAG, "Device unregistered ");
        if (unregistered) {
            mDisposable.dispose();
            mSubscriberHandler.dispose();
        }
    }

    @Override
    public boolean onMessage( Context context, Intent intent ) {
        String from = intent.getStringExtra(Constants.PUBLISHER_KEY);
        if (from != null && from.equals(LoggedUser.getInstance().getUserIdLogged())) {
            //don't care about my own event messages
            return false;
        }

        String recipient = intent.getStringExtra(Constants.RECIPIENT_KEY);
        if (recipient != null &&
            (recipient.equals(LoggedUser.getInstance().getUserIdLogged()) ||
                    recipient.equals(Constants.PUBLISH_ALL))) {
            String messageType = intent.getStringExtra(Constants.MESSAGE_TYPE_KEY);
            String message = intent.getStringExtra(Constants.MESSAGE_KEY);
            if (mSubscriberHandler == null) {
                initSubscriber(context);
            }
            mSubscriberHandler.processIncomingMessage(messageType, message);
        }
        // When returning 'true', default Backendless onMessage implementation will be executed.
        // The default implementation displays the notification in the Android Notification Center.
        // Returning false, cancels the execution of the default implementation.
        return false;
    }

    @Override
    public void onError( Context context, String message ) {
        Log.e(TAG, "Error registering device: " + message);
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
