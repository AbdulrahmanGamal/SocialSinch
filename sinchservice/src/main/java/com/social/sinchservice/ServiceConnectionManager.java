package com.social.sinchservice;

import android.content.Context;
import android.content.Intent;

import com.social.sinchservice.model.ChatMessage;

import java.util.List;

/**
 * Singleton to handle the communication with the Local Service
 */

public class ServiceConnectionManager {
    private SinchServiceConnection mConnection;
    private static ServiceConnectionManager mServiceConnectionManager;

    public static ServiceConnectionManager getInstance(Context context, String currentUser) {
        if (mServiceConnectionManager == null) {
            mServiceConnectionManager = new ServiceConnectionManager(context, currentUser);
        }
        return mServiceConnectionManager;
    }
    private ServiceConnectionManager(Context context, String currentUser) {
        this.mConnection = new SinchServiceConnection(context, currentUser);
        Intent intent = new Intent(context, SinchService.class);
        intent.putExtra(SinchService.CURRENT_USER_KEY, currentUser);
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Send a chat message to the Chat Server
     * @param recipientIds
     * @param message
     */
    public void sendMessage(List<String> recipientIds, String message) {
        mConnection.sendMessage(recipientIds, message);
    }

    public void removeMessageClientListener(Context context) {
        mConnection.removeMessageClientListener(context);
    }

    public List<ChatMessage> retrieveLastMessages(String senderId,
                                                  String recipientId,
                                                  int max) {
        return mConnection.retrieveLastMessages(senderId, recipientId, max);
    }
}