package com.social.valgoodchat.push;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.backendless.push.BackendlessPushService;
import com.social.backendless.PublishSubscribeHandler;
import com.social.backendless.utils.Constants;
import com.social.backendless.utils.LoggedUser;
import com.social.valgoodchat.utils.NotificationUtils;

/**
 * Service to process the offline messages received
 */
public class SocialPushService extends BackendlessPushService {
    private static final String TAG = "SocialPushService";

    @Override
    public void onRegistered(Context context, String registrationId) {
        Log.d(TAG, "Device registered " + registrationId);
    }

    @Override
    public void onUnregistered( Context context, Boolean unregistered ) {
        Log.d(TAG, "Device unregistered ");
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
            PublishSubscribeHandler.getInstance(context).processIncomingMessage(messageType, message);
            if (messageType != null && messageType.equals(Constants.MESSAGE_TYPE_CHAT_KEY)) {
                NotificationUtils.getInstance().showNotificationIfNeeded(context, message);
            }
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
}
