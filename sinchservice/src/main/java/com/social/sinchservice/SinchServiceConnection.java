package com.social.sinchservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.social.sinchservice.model.ChatMessage;

import java.util.List;

/**
 * Class that encapsulates all the connection with the Sinch service to send and receive
 * chat messages.
 */

public class SinchServiceConnection implements ServiceConnection {
    private SinchService.MessageServiceInterface mMessageService;
    private static SinchServiceConnection mSinchServiceConnection;

    public static SinchServiceConnection getInstance() {
        if (mSinchServiceConnection == null) {
            mSinchServiceConnection = new SinchServiceConnection();
        }
        return mSinchServiceConnection;
    }

    /**
     * Bind to the service to send and receive information
     */
    public void bindToService(Context context) {
        Intent intent = new Intent(context, SinchService.class);
        context.bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mMessageService = (SinchService.MessageServiceInterface) iBinder;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mMessageService = null;
    }
    /**
     * send message to recipient
     */
    public void sendMessage(List<String> recipientIds, String message) {
        for (String recipientId: recipientIds) {
            mMessageService.sendMessage(recipientId, message);
        }
    }
    /**
     * Stop listening for messages
     */
    public void unbindService(Context context) {
        context.unbindService(this);
    }

//    public List<ChatMessage> retrieveLastMessages(String senderId,
//                                                  String recipientId,
//                                                  int max) {
//        return mMessageService.retrieveLastMessages(senderId, recipientId, max);
//    }
}
