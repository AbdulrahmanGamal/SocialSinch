package com.social.sinchservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.social.sinchservice.model.ChatMessage;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Class that encapsulates all the connection with the Sinch service to send and receive
 * chat messages. It posts messages to the RxBus to notify observers
 */

public class SinchServiceConnection implements ServiceConnection {
    private SinchService.MessageServiceInterface mMessageService;
    private SinchMessageClientListener mMessageClientListener;

    public SinchServiceConnection(final Context context, final String currentUser) {
        this.mMessageClientListener = new SinchMessageClientListener(context, currentUser);
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

    public List<ChatMessage> retrieveLastMessages(String senderId,
                                                  String recipientId,
                                                  int max) {
        return mMessageClientListener.retrieveLastMessages(senderId, recipientId, max);
    }
}
