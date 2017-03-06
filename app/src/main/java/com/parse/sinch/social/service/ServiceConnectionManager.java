package com.parse.sinch.social.service;

import android.content.Context;
import android.content.Intent;
import java.util.List;

/**
 * Singleton to handle the communication with the Local Service
 */

public class ServiceConnectionManager {
    private SinchServiceConnection mConnection;

    public ServiceConnectionManager(Context context) {
        this.mConnection = new SinchServiceConnection(context);
        context.bindService(new Intent(context, SinchService.class),
                mConnection, Context.BIND_AUTO_CREATE);
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
}