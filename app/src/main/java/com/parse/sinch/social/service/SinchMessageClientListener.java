package com.parse.sinch.social.service;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.backendless.Backendless;
import com.parse.sinch.social.database.ChatBriteDataSource;
import com.parse.sinch.social.model.ChatMessage;
import com.parse.sinch.social.bus.RxOutgoingMessageBus;
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
    private ChatBriteDataSource mChatDataSource;
    private RxOutgoingMessageBus mMessageBus = RxOutgoingMessageBus.getInstance();

    public SinchMessageClientListener(Context context) {
        mChatDataSource = new ChatBriteDataSource(context);
    }

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
                return Backendless.UserService.loggedInUser();
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
        //in case of RECEIVED we have to swap the send id and recipient id because the saving in the
        //database is user1:user2:totalmessage even of user1 is receiving the message
        Log.e(TAG, "ADDING MESSAGE: " + chatMessage);
        Long messageId;

        try {
            switch (chatMessage.getStatus()) {
                case SENT:
                    messageId = mChatDataSource.verifyMessage(chatMessage.getSenderId(),
                            chatMessage.getRecipientIds().get(0),
                            chatMessage.getTextBody());
                    if (messageId == -1) {
                        messageId = mChatDataSource.addNewMessage(chatMessage.getSenderId(),
                                chatMessage.getRecipientIds().get(0),
                                chatMessage.getTextBody(),
                                chatMessage.getStatus(),
                                chatMessage.getSentId());

                        chatMessage.setMessageId(messageId);
                        mMessageBus.setMessage(chatMessage);
                    } else {
                        Log.e(TAG, "SKIPPING ALREADY SAVED MESSAGE!!!");
                    }
                    break;
                case DELIVERED:
                    //update the status in DB
                    messageId = mChatDataSource.verifySentMessage(chatMessage.getSentId());
                    if (messageId != -1) {
                        mChatDataSource.updateMessageStatus(messageId, ChatMessage.ChatStatus.DELIVERED);
                    } else {
                        Log.e(TAG, "DELIVERED BUT MESSAGE NOT IN DB!!!");
                    }
                    break;
                case RECEIVED:
                    messageId = mChatDataSource.verifyMessage(chatMessage.getRecipientIds().get(0),
                            chatMessage.getSenderId(),
                            chatMessage.getTextBody());
                    if (messageId == -1) {
                        messageId = mChatDataSource.addNewMessage(chatMessage.getSenderId(),
                                chatMessage.getRecipientIds().get(0),
                                chatMessage.getTextBody(),
                                chatMessage.getStatus(), null);
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
}
