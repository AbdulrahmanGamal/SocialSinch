package com.social.sinchservice;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;
import com.social.sinchservice.bus.RxOutgoingMessageBus;
import com.social.sinchservice.database.ChatBriteDataSource;
import com.social.sinchservice.model.ChatMessage;
import com.social.sinchservice.model.ChatStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Handles the incoming/outgoing messages
 */

class SinchMessageClientListener implements MessageClientListener {

    private static final String TAG = "SinchMsgClientListener";
    private ChatBriteDataSource mChatDataSource;
    private RxOutgoingMessageBus mMessageBus = RxOutgoingMessageBus.getInstance();
    private String mCurrentUser;

    public SinchMessageClientListener(Context context, String currentUser) {
        this.mChatDataSource = new ChatBriteDataSource(context);
        this.mCurrentUser = currentUser;
    }

    @Override
    public void onMessageFailed(MessageClient client, Message message,
                                MessageFailureInfo failureInfo) {
        processMessage(message, ChatStatus.FAILED);
        Log.d(TAG, "Msg failed: " + failureInfo.toString());
    }

    @Override
    public void onIncomingMessage(MessageClient client, Message message) {
        // Display an incoming message
        processMessage(message, ChatStatus.RECEIVED);
    }

    @Override
    public void onMessageSent(MessageClient client, Message message,
                              String recipientId) {
        // Display the message that was just sent
        Log.e(TAG, "onMessageSEnt CALLED: " + recipientId + " - " + message.getTextBody());
        processMessage(message, ChatStatus.SENT);

    }

    @Override
    public void onMessageDelivered(MessageClient client,
                                   MessageDeliveryInfo deliveryInfo) {
        processMessage(createDeliveryMessage(deliveryInfo), ChatStatus.DELIVERED);
    }

    // Don't worry about this right now
    @Override
    public void onShouldSendPushData(MessageClient client, Message message,
                                     List<PushPair> pushPairs) {
        Log.e(TAG, "onShouldSendPushData");
    }

    private void processMessage(Message message, ChatStatus status) {
        ChatMessage chatMessageInfo = new ChatMessage();
        chatMessageInfo.setRecipientIds(message.getRecipientIds());
        chatMessageInfo.setSenderId(message.getSenderId());
        chatMessageInfo.setTextBody(message.getTextBody());
        chatMessageInfo.setTimestamp(message.getTimestamp());
        chatMessageInfo.setStatus(status);
        chatMessageInfo.setSentId(message.getMessageId());
        addMessageToDB(chatMessageInfo);
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
                return mCurrentUser;
            }

            @Override
            public Date getTimestamp() {
                return deliveryInfo.getTimestamp();
            }
        };
    }

    /**
     * Save the message in the local DB
     * @param chatMessage
     */
    private synchronized void addMessageToDB(final ChatMessage chatMessage) {
        Long messageId;
        try {
            switch (chatMessage.getStatus()) {
                case SENT:
                    messageId = mChatDataSource.verifyMessageBySentID(chatMessage.getSenderId(),
                            chatMessage.getRecipientIds().get(0),
                            chatMessage.getSentId());
                    if (messageId == -1) {
                        messageId = mChatDataSource.addNewMessage(chatMessage);
                        chatMessage.setMessageId(messageId);
                        mMessageBus.setMessage(chatMessage);
                    } else {
                        //Log.e(TAG, "SKIPPING " + chatMessage.getTextBody());
                    }
                    break;
                case DELIVERED:
                    //update the status in DB
                    messageId = mChatDataSource.verifyMessageBySentID(chatMessage.getSenderId(),
                            chatMessage.getRecipientIds().get(0),
                            chatMessage.getSentId());
                    if (messageId != -1) {
                        mChatDataSource.updateMessageStatus(messageId, ChatStatus.DELIVERED);
                    } else {
                        Log.e(TAG, "GOT DELIVERED BUT MESSAGE NOT IN DB!!! " + chatMessage.getTextBody());
                    }
                    break;
                case RECEIVED:
                    messageId = mChatDataSource.verifyMessageByDate(chatMessage.getRecipientIds().get(0),
                            chatMessage.getSenderId(),
                            chatMessage.getTimestamp(), chatMessage.getTextBody());
                    if (messageId == -1) {
                        Log.e(TAG, "ADDING RECEIVED MESSAGE: " + chatMessage.getTimestamp()
                                + " - " + chatMessage.getTextBody());
                        messageId = mChatDataSource.addNewMessage(chatMessage);
                        chatMessage.setMessageId(messageId);
                        mMessageBus.setMessage(chatMessage);
                    } else {
                        Log.e(TAG, "SKIPPING ALREADY RECEIVED SAVED MESSAGE!!!");
                    }
                    break;

            }

        } catch (SQLException ex) {
            Log.e(TAG, "Error adding the new Message: " + ex.getMessage());
        }

    }

    /**
     * Exposed the retrieve message method to the outside classes
     * @param senderId
     * @param recipientId
     * @param max
     * @return
     */
    public List<ChatMessage> retrieveLastMessages(String senderId,
                                                  String recipientId,
                                                  int max) {
        return mChatDataSource.retrieveLastMessages(senderId, recipientId, max);
    }
}
