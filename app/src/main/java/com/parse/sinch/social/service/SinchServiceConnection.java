package com.parse.sinch.social.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.sinch.android.rtc.messaging.MessageClientListener;

import java.util.List;

/**
 * Class that encapsulates all the connection with the Sinch service to send and receive
 * chat messages. It posts messages to the RxBus to notify observers
 */

public class SinchServiceConnection implements ServiceConnection {
    private SinchService.MessageServiceInterface mMessageService;
    private MessageClientListener mMessageClientListener;

    public SinchServiceConnection() {
        this.mMessageClientListener = new SinchMessageClientListener();

    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mMessageService = (SinchService.MessageServiceInterface) iBinder;
        mMessageService.addMessageClientListener(mMessageClientListener);
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
    public void removeMessageClientListener(Context context) {
        mMessageService.removeMessageClientListener(mMessageClientListener);
        context.unbindService(this);
    }
}
