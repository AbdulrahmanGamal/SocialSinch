package com.parse.sinch.social.service;

import android.util.Log;

import com.parse.sinch.social.model.ChatMessage;
import com.parse.sinch.social.model.RxOutgoingMessageBus;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Handles the incoming/outgoing messages
 */

class SinchMessageClientListener implements MessageClientListener {

    private static final String TAG = "SinchMsgClientListener";
    private RxOutgoingMessageBus mMessageBus = RxOutgoingMessageBus.getInstance();

    @Override
    public void onMessageFailed(MessageClient client, Message message,
                                MessageFailureInfo failureInfo) {
        processMessage(message, ChatMessage.ChatStatus.FAILED);
        Log.d(TAG, "Msg failed: " + failureInfo.toString());
    }

    @Override
    public void onIncomingMessage(MessageClient client, Message message) {
        // Display an incoming message
        processMessage(message, ChatMessage.ChatStatus.RECEIVED);
    }

    @Override
    public void onMessageSent(MessageClient client, Message message,
                              String recipientId) {
        // Display the message that was just sent
        processMessage(message, ChatMessage.ChatStatus.SENT);

    }

    @Override
    public void onMessageDelivered(MessageClient client,
                                   MessageDeliveryInfo deliveryInfo) {
        processMessage(createDeliveryMessage(deliveryInfo), ChatMessage.ChatStatus.DELIVERED);
    }

    // Don't worry about this right now
    @Override
    public void onShouldSendPushData(MessageClient client, Message message,
                                     List<PushPair> pushPairs) {
        Log.e(TAG, "onShouldSendPushData");
    }

    private void processMessage(Message message, ChatMessage.ChatStatus status) {
        ChatMessage chatMessageInfo = new ChatMessage();
        chatMessageInfo.setMessageId(message.getMessageId());
        chatMessageInfo.setRecipientIds(message.getRecipientIds());
        chatMessageInfo.setSenderId(message.getSenderId());
        chatMessageInfo.setTextBody(message.getTextBody());
        chatMessageInfo.setTimestamp(message.getTimestamp());
        chatMessageInfo.setStatus(status);
        mMessageBus.setMessage(chatMessageInfo);
    }

    private Message createDeliveryMessage(final MessageDeliveryInfo deliveryInfo) {
        return new Message() {
            @Override
            public String getMessageId() {
                return deliveryInfo.getMessageId();
            }

            @Override
            public Map<String, String> getHeaders() {
                return null;
            }

            @Override
            public String getTextBody() {
                return null;
            }

            @Override
            public List<String> getRecipientIds() {
                List<String> ids = new ArrayList<>();
                ids.add(deliveryInfo.getRecipientId());
                return ids;
            }

            @Override
            public String getSenderId() {
                return null;
            }

            @Override
            public Date getTimestamp() {
                return deliveryInfo.getTimestamp();
            }
        };
    }
}
