package com.social.sinchservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

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
    private static SinchServiceConnection mSinchServiceConnection;
    private Intent broadcastIntent = new Intent("com.parse.sinch.social.TabActivity");
    private LocalBroadcastManager broadcaster;

    public static SinchServiceConnection getInstance(final Context context, final String currentUser) {
        if (mSinchServiceConnection == null) {
            mSinchServiceConnection = new SinchServiceConnection(context, currentUser);
        }
        return mSinchServiceConnection;
    }

    private SinchServiceConnection(final Context context, final String currentUser) {
        this.broadcaster = LocalBroadcastManager.getInstance(context);
        Intent intent = new Intent(context, SinchService.class);
        intent.putExtra(SinchService.CURRENT_USER_KEY, currentUser);
        context.bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mMessageService = (SinchService.MessageServiceInterface) iBinder;
        broadcastIntent.putExtra("success", true);
        broadcaster.sendBroadcast(broadcastIntent);

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mMessageService = null;
        broadcastIntent.putExtra("success", false);
        broadcaster.sendBroadcast(broadcastIntent);
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

    public List<ChatMessage> retrieveLastMessages(String senderId,
                                                  String recipientId,
                                                  int max) {
        return mMessageService.retrieveLastMessages(senderId, recipientId, max);
    }
}
