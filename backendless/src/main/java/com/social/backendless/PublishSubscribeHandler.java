package com.social.backendless;

import android.content.Context;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.Subscription;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.Message;
import com.backendless.messaging.SubscriptionOptions;
import com.social.backendless.data.ChatMessageManager;
import com.social.backendless.data.EventMessageManager;
import com.social.backendless.utils.Constants;

import java.util.List;

/**
 * Singleton to handle the subscription to the channel and the publishing
 */
public class PublishSubscribeHandler {

    private static final String TAG = "PublishSubscribeHandler";
    private Subscription mSubscription;
    private static PublishSubscribeHandler sPublishSubscriberHandler;
    private ChatMessageManager mChatMessageManager;
    private EventMessageManager mEventMessageManager;
    private String mUserLogged;

    public static PublishSubscribeHandler getInstance(Context context, String userLogged) {
        if (sPublishSubscriberHandler == null) {
           sPublishSubscriberHandler = new PublishSubscribeHandler(context, userLogged);
        }
        return sPublishSubscriberHandler;
    }

    private PublishSubscribeHandler(Context context, String userLogged) {
        this.mChatMessageManager = new ChatMessageManager(context, userLogged);
        this.mEventMessageManager = new EventMessageManager();
        this.mUserLogged = userLogged;
    }

    /**
     * Subscribes ti the default channel
     */
//    public void subscribe() {
//        SubscriptionOptions subscriptionOptions = new SubscriptionOptions();
//        subscriptionOptions.setSelector(Constants.RECIPIENT_KEY + " = '" + mUserLogged + "'");
//        Backendless.Messaging.subscribe(Constants.DEFAULT_CHANNEL,
//                new AsyncCallback<List<Message>>() {
//                    @Override
//                    public void handleResponse( List<Message> response ) {
//                        for (Message messageReceived: response) {
//                            if (messageReceived.getHeaders().get(Constants.MESSAGE_TYPE_KEY).
//                                    equals(Constants.MESSAGE_TYPE_CHAT_KEY)) {
//                                mChatMessageManager.processIncomingMessage(messageReceived);
//                            } else {
//                                mEventMessageManager.processIncomingEvent(messageReceived);
//                            }
//                        }
//                    }
//                    @Override
//                    public void handleFault( BackendlessFault fault ) {
//                        Log.e(TAG, "Fault Receiving message : " + fault);
//                    }
//                }, subscriptionOptions ,
//                new AsyncCallback<Subscription>() {
//                    @Override
//                    public void handleResponse( Subscription response )
//                    {
//                        mSubscription = response;
//                    }
//                    @Override
//                    public void handleFault( BackendlessFault fault )
//                    {
//                        Log.e(TAG, "Subscription fault : " + fault);
//                    }
//                }
//        );
//    }

    public void unsubscribe() {
        if (mSubscription != null) {
            mSubscription.cancelSubscription();
            mChatMessageManager.dispose();
        }
    }

    public void pauseSubscription() {
        if (mSubscription != null) {
            mSubscription.pauseSubscription();
        }
    }

    public void cancelSubscription() {
        if (mSubscription != null) {
            mSubscription.cancelSubscription();
        }
    }
}
