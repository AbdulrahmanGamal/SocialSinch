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
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
        final ChatMessage chatMessageInfo = new ChatMessage();
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
    private Long addMessageToDB(final ChatMessage chatMessage) {
        Long messageId = -1L;
        try {
            switch (chatMessage.getStatus()) {
                case SENT:
                    Observable.fromCallable(new Callable<Long>() {
                        @Override
                        public Long call() throws Exception {
                            return mChatDataSource.verifyMessageBySentID(chatMessage.getSenderId(),
                                    chatMessage.getRecipientIds().get(0),
                                    chatMessage.getSentId());
                        }
                    }).subscribeOn(Schedulers.io())
                      .filter(new Func1<Long, Boolean>() {
                          @Override
                          public Boolean call(Long messageId) {
                              return messageId == -1;
                          }
                      }).map(new Func1<Long, Long>() {
                        @Override
                        public Long call(Long messageId) {
                            return mChatDataSource.addNewMessage(chatMessage);
                        }
                    }).subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<Long>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "Error: " + e.getMessage());
                            }

                            @Override
                            public void onNext(Long messageId) {
                                if (mMessageBus.hasObservers()) {
                                    Log.e(TAG, "Added Message # " + messageId);
                                    chatMessage.setMessageId(messageId);
                                    mMessageBus.setMessage(chatMessage);
                                } else {
                                    Log.e(TAG, "Saved Message # " + messageId);
                                }
                            }
                        });
//                    messageId = mChatDataSource.verifyMessageBySentID(chatMessage.getSenderId(),
//                            chatMessage.getRecipientIds().get(0),
//                            chatMessage.getSentId());
//                    if (messageId == -1) {
//                        messageId = mChatDataSource.addNewMessage(chatMessage);
//                        chatMessage.setMessageId(messageId);
//                        if (mMessageBus.hasObservers()) {
//                            mMessageBus.setMessage(chatMessage);
//                        }
//                    } else {
//                        //Log.e(TAG, "SKIPPING " + chatMessage.getTextBody());
//                    }
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
                        if (mMessageBus.hasObservers()) {
                            mMessageBus.setMessage(chatMessage);
                        }
                    } else {
                        Log.e(TAG, "SKIPPING ALREADY RECEIVED SAVED MESSAGE!!!");
                    }
                    break;

            }

        } catch (SQLException ex) {
            Log.e(TAG, "Error adding the new Message: " + ex.getMessage());
        }

        return messageId;
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
