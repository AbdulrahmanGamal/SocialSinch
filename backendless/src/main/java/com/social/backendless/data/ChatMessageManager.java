package com.social.backendless.data;

import android.content.Context;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.messaging.Message;
import com.backendless.services.messaging.MessageStatus;
import com.google.gson.Gson;
import com.social.backendless.bus.RxIncomingMessageBus;
import com.social.backendless.bus.RxOutgoingMessageBus;
import com.social.backendless.database.ChatBriteDataSource;
import com.social.backendless.model.ChatMessage;
import com.social.backendless.model.ChatStatus;
import com.social.backendless.utils.Constants;
import com.social.backendless.utils.ObservableUtils;

import java.util.Calendar;
import java.util.Locale;

import io.reactivex.functions.Consumer;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Class to encapsulate all the logic to convert the chat messages sent and received
 */

public class ChatMessageManager {
    private ChatBriteDataSource mDataSource;

    private static final String TAG = "ChatMessageManager";

    public ChatMessageManager(Context context) {
        this.mDataSource = ChatBriteDataSource.getInstance(context);
        configureIncomingMessageBus();
    }

    /**
     * Listen for messages coming from the Views
     */
    private void configureIncomingMessageBus() {
        RxIncomingMessageBus.getInstance().getMessageObservable().subscribe(new Consumer<ChatMessage>() {
            @Override
            public void accept(ChatMessage chatMessage) throws Exception {
                processOutgoingMessage(chatMessage, ChatStatus.SEND);
            }
        });
    }
    /**
     * Sends an instant message to the default channel
     * @param message
     */
    public void processOutgoingMessage(ChatMessage message,
                                       ChatStatus status) {
        final ChatMessage chatMessageInfo = completeChatMessage(message, status);

        Gson gson = new Gson();
        String jsonMessage = gson.toJson(chatMessageInfo);

        ObservableUtils.getPublisher(jsonMessage,
                                     chatMessageInfo.getSenderId(),
                                     chatMessageInfo.getRecipientId(),
                                     Constants.MESSAGE_TYPE_CHAT_KEY).filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object result) {
                //skip the notification received for DELIVERED messages
                return  result instanceof MessageStatus &&
                        !chatMessageInfo.getStatus().equals(ChatStatus.DELIVERED);
            }
        }).subscribe(new Action1<Object>() {
            @Override
            public void call(Object result) {
                if (result instanceof MessageStatus) {
                    Log.e(TAG, "Message was published " + result);
                    notifyViews(chatMessageInfo, ChatStatus.SENT);
                } else {
                    Log.e(TAG, "Message was not published: " + result);
                    notifyViews(chatMessageInfo, ChatStatus.FAILED);
                }
            }
        });
    }
    /**
     * Notify the views about the event using the rx bus
     * @param message
     */
    public synchronized void notifyViews(ChatMessage message, ChatStatus status) {
        switch (status) {
            case SENT:
            case FAILED:
                mDataSource.updateMessageStatus(message.getMessageId(), status);
                break;
            case RECEIVED:
                message.setStatus(ChatStatus.RECEIVED);
                Long messageId = mDataSource.addNewMessage(message);
                message.setMessageId(messageId);
                break;
            case DELIVERED:
                //change the status in DB to flag the previous SENT now to DELIVERED
                mDataSource.updateMessageStatus(message.getMessageId(), status);
                break;
        }
        RxOutgoingMessageBus messageBus = RxOutgoingMessageBus.getInstance();
        if (messageBus.hasObservers()) {
            message.setStatus(status);
            messageBus.setMessage(message);
        }
    }
    /**
     * Process the receive message and execute the according action depending of the status
     * @param message
     */
    public void processIncomingMessage(Message message) {
        final Gson gson = new Gson();
        //Process only if its CHAT
        if (message.getHeaders().get(Constants.MESSAGE_TYPE_KEY).equals(Constants.MESSAGE_TYPE_CHAT_KEY)) {
            Log.e(TAG, "Received message : " + message);
            ChatMessage messageReceived = gson.fromJson((String) message.getData(), ChatMessage.class);
            switch (messageReceived.getStatus()) {
                case SEND:
                    ChatMessage deliveredMessage = new ChatMessage(messageReceived.getSenderId(), "");
                    deliveredMessage.setMessageId(messageReceived.getMessageId());
                    processOutgoingMessage(deliveredMessage, ChatStatus.DELIVERED);
                    notifyViews(messageReceived, ChatStatus.RECEIVED);
                    break;
                case DELIVERED:
                    notifyViews(messageReceived, ChatStatus.DELIVERED);
                    break;
                //TODO consider the other type of cases
                default:
                    break;

            }
        }
    }
    /**
     * Complete the original chat message created in the view with the rest
     * of the information after the message sent to the server
     *
     * @param message
     * @return
     */
    private ChatMessage completeChatMessage(ChatMessage message,  ChatStatus status) {
        message.setSenderId(Backendless.UserService.loggedInUser());
        message.setTimestamp(Calendar.getInstance(Locale.getDefault()).getTime());
        message.setStatus(status);

        if (status.equals(ChatStatus.SEND)) {
            //before sending the message, save it in Db to assign message id from now on
            final long messageId = mDataSource.addNewMessage(message);
            message.setMessageId(messageId);
        }

        return message;
    }
}
