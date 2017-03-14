package com.social.sinchservice;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.Subscription;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.Message;
import com.backendless.messaging.PublishOptions;
import com.backendless.messaging.SubscriptionOptions;
import com.backendless.services.messaging.MessageStatus;
import com.social.sinchservice.model.ChatMessage;
import com.social.sinchservice.model.ChatStatus;
import com.social.sinchservice.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by valgood on 3/13/2017.
 */

public class PublishMessage {

    private static final String TAG = "PublishMessage";
    private static final String DEFAULT_CHANNEL = "default";
    private Subscription subscription;

    public PublishMessage() {
        subcribe();
    }
    public void sendMessage(String recipientUserId, String textBody) {
        final ChatMessage chatMessageInfo = new ChatMessage();
        String senderId = Backendless.UserService.loggedInUser();
        List<String> recipientIds = new ArrayList<>();
        recipientIds.add(recipientUserId);
        chatMessageInfo.setRecipientIds(recipientIds);
        chatMessageInfo.setSenderId(senderId);
        chatMessageInfo.setTextBody(textBody);
        chatMessageInfo.setTimestamp(Calendar.getInstance(Locale.getDefault()).getTime());
        chatMessageInfo.setStatus(ChatStatus.SENT);
        chatMessageInfo.setSentId("");

        Backendless.Messaging.publish(DEFAULT_CHANNEL, textBody, new PublishOptions( "0280CFFE-6C36-D6F2-FFF1-6BF559C87900" ),  new AsyncCallback<MessageStatus>() {
            @Override
            public void handleResponse(MessageStatus messageStatus) {
                Log.e(TAG, "Message was Published");
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.e(TAG, "Message was NOT Published");
            }
        });
    }

    public void subcribe() {
        Backendless.Messaging.subscribe(
                DEFAULT_CHANNEL,
                new AsyncCallback<List<Message>>()
                {
                    @Override
                    public void handleResponse( List<Message> response )
                    {
                        for ( Message message : response )
                            Log.e(TAG, "Received message : " + message);
                    }

                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        Log.e(TAG, "fault message : " + fault);
                    }
                },
                new AsyncCallback<Subscription>()
                {
                    @Override
                    public void handleResponse( Subscription response )
                    {
                        subscription = response;
                    }

                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        Log.e(TAG, "Subscription fault : " + fault);
                    }
                }
        );
    }
}
