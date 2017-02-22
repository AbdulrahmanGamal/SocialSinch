package com.parse.sinch.social.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.parse.sinch.social.model.ChatInfo;
import com.parse.sinch.social.model.RxMessageBus;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;

import java.util.List;

/**
 * Created by valgood on 2/21/2017.
 */

public class SinchServiceConnection implements ServiceConnection {
    private SinchService.MessageServiceInterface mMessageService;
    private MessageClientListener mMessageClientListener;
    private Context mContext;
    private RxMessageBus mMessageBus;

    public SinchServiceConnection(Context context) {
        this.mContext = context;
        this.mMessageClientListener = new SinchMessageClientListener();
        this.mMessageBus = RxMessageBus.getInstance();
        context.bindService(new Intent(context, SinchService.class), this, Context.BIND_AUTO_CREATE);
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
     * send message to recepient
     */
    public void sendMessage(String recipientId, String message) {
        //mMessageService.sendMessage(recipientId, message);
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setChatId(1);
        chatInfo.setMessage(message);
        chatInfo.setStatus(ChatInfo.ChatStatus.DELIVERED);
        mMessageBus.setMessage(chatInfo);
    }
    /**
     * Stop listening for messages
     */
    public void removeMessageClientListener() {
        mMessageService.removeMessageClientListener(mMessageClientListener);
        mContext.unbindService(this);
    }
}

class SinchMessageClientListener implements MessageClientListener {

    private static final String TAG = "SinchMsgClientListener";

    @Override
    public void onMessageFailed(MessageClient client, Message message,
                                MessageFailureInfo failureInfo) {
//        Toast.makeText(mContext, "Message failed to send.",
//                Toast.LENGTH_LONG).show();
        Log.d(TAG, "Msg failed: " + failureInfo.toString());
    }

    @Override
    public void onIncomingMessage(MessageClient client, Message message) {
        // Display an incoming message

    }

    @Override
    public void onMessageSent(MessageClient client, Message message,
                              String recipientId) {
        // Display the message that was just sent

        // Later, I'll show you how to store the
        // Message in Parse, so you can retrieve and
        // display them every time the conversation is opened

    }

    @Override
    public void onMessageDelivered(MessageClient client,
                                   MessageDeliveryInfo deliveryInfo) {

    }

    // Don't worry about this right now
    @Override
    public void onShouldSendPushData(MessageClient client, Message message,
                                     List<PushPair> pushPairs) {

    }
}
