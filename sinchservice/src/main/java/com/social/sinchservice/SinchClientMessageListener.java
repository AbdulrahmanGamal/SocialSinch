package com.social.sinchservice;

import android.content.Context;
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
import rx.Emitter;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Listener class to send/receive the messages and save them in DB
 */

public class SinchClientMessageListener {
    private static final String TAG = "SinchClientMessageListe";
    private RxOutgoingMessageBus mMessageBus = RxOutgoingMessageBus.getInstance();
    private ChatBriteDataSource mChatDataSource;
    private String mCurrentUser;

    public SinchClientMessageListener(Context context, String currentUser) {
        this.mChatDataSource = new ChatBriteDataSource(context);
        this.mCurrentUser = currentUser;
    }

    public void observableMessageClientListenerWrapper(final MessageClient messageClient) {
        Observable.fromEmitter(new Action1<Emitter<ChatMessage>>() {
            @Override
            public void call(final Emitter<ChatMessage> emitter) {
                MessageClientListener messageClientListener = new MessageClientListener() {
                    @Override
                    public void onIncomingMessage(MessageClient messageClient, Message message) {
                        emitter.onNext(processMessage(message, ChatStatus.RECEIVED));
                    }

                    @Override
                    public void onMessageSent(MessageClient messageClient, Message message, String s) {
                        processMessage(message, ChatStatus.SENT);
                        emitter.onNext(processMessage(message, ChatStatus.SENT));
                    }

                    @Override
                    public void onMessageFailed(MessageClient messageClient, Message message, MessageFailureInfo messageFailureInfo) {
                        processMessage(message, ChatStatus.FAILED);
                        emitter.onNext(processMessage(message, ChatStatus.FAILED));
                    }

                    @Override
                    public void onMessageDelivered(MessageClient messageClient, MessageDeliveryInfo messageDeliveryInfo) {
                        ChatMessage chatMessage = processMessage(createDeliveryMessage(messageDeliveryInfo), ChatStatus.DELIVERED);
                        emitter.onNext(chatMessage);
                    }

                    @Override
                    public void onShouldSendPushData(MessageClient messageClient, Message message, List<PushPair> list) {

                    }
                };
                messageClient.addMessageClientListener(messageClientListener);

            }
        }, Emitter.BackpressureMode.BUFFER).filter(new Func1<ChatMessage, Boolean>() {
            @Override
            public Boolean call(ChatMessage chatMessage) {
                //if the message contains the same recipients, timestamp and text
                //then filter it out
                return mChatDataSource.verifyMessageByDate(chatMessage) == -1;
            }
        }).subscribe(new Action1<ChatMessage>() {
            @Override
            public void call(ChatMessage message) {
                addMessageToDB(message);
           }
        });
    }

    /**
     * Converts the MEssage into a ChatMessage object
     * @param message
     * @param status
     * @return
     */
    private ChatMessage processMessage(Message message, ChatStatus status) {
        final ChatMessage chatMessageInfo = new ChatMessage();
        chatMessageInfo.setRecipientIds(message.getRecipientIds());
        chatMessageInfo.setSenderId(message.getSenderId());
        chatMessageInfo.setTextBody(message.getTextBody());
        chatMessageInfo.setTimestamp(message.getTimestamp());
        chatMessageInfo.setStatus(status);
        chatMessageInfo.setSentId(message.getMessageId());
        return chatMessageInfo;
    }

    /**
     * For DeliveryInfo message, create a Message
     * @param deliveryInfo
     * @return
     */
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
                return "";
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

    private void addMessageToDB(ChatMessage chatMessage) {
        Long messageId;
        switch (chatMessage.getStatus()) {
            case SENT:
            case RECEIVED:
                messageId = mChatDataSource.addNewMessage(chatMessage);
                notifyViewSubscribers(messageId, chatMessage);
                break;
            case DELIVERED:
                messageId = mChatDataSource.verifyMessageBySentID(chatMessage.getSenderId(),
                                            chatMessage.getRecipientIds().get(0),
                                            chatMessage.getSentId());
                if (messageId != -1) {
                    mChatDataSource.updateMessageStatus(messageId, chatMessage.getStatus());
                    notifyViewSubscribers(messageId, chatMessage);
                } else {
                    Log.e(TAG, "Skipping delivered without sentId previously saved");
                }
                break;
        }
    }

    /**
     * Send the message added in DB to the UI to display
     * @param messageId
     * @param chatMessage
     */
    private void notifyViewSubscribers(Long messageId, ChatMessage chatMessage) {
        if (mMessageBus.hasObservers()) {
            chatMessage.setMessageId(messageId);
            mMessageBus.setMessage(chatMessage);
        }
    }

    public List<ChatMessage> retrieveLastMessages(String senderId, String recipientId, int max) {
       return mChatDataSource.retrieveLastMessages(senderId, recipientId, max);
    }
}
