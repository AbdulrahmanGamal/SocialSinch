package com.social.backendless;

import android.content.Context;
import android.util.Log;

import com.social.backendless.data.ChatMessageManager;
import com.social.backendless.data.EventMessageManager;
import com.social.backendless.utils.Constants;

/**
 * Singleton to handle the subscription to the channel and the publishing
 */
public class PublishSubscribeHandler {

    private static final String TAG = "PublishSubscribeHandler";
    private static PublishSubscribeHandler sPublishSubscriberHandler;
    private ChatMessageManager mChatMessageManager;
    private EventMessageManager mEventMessageManager;

    public static PublishSubscribeHandler getInstance(Context context) {
        initialize(context);
        return sPublishSubscriberHandler;
    }

    public static void initialize(Context context) {
        if (sPublishSubscriberHandler == null) {
            sPublishSubscriberHandler = new PublishSubscribeHandler(context);
        }
    }
    private PublishSubscribeHandler(Context context) {
        Log.e(TAG, "Creating instance of PublishSubscribeHandler");
        this.mChatMessageManager = new ChatMessageManager(context);
        this.mEventMessageManager = new EventMessageManager();
    }

    public void processIncomingMessage(String messageType, String messageReceived) {
        if (messageType == null) {
            return;
        }
        //apply the correct parsing based on the message type
        if ( messageType.equals(Constants.MESSAGE_TYPE_CHAT_KEY)) {
            mChatMessageManager.processIncomingMessage(messageReceived);
        } else {
            mEventMessageManager.processIncomingEvent(messageReceived);
        }
    }

//    public void dispose() {
//        mChatMessageManager.dispose();
//        mEventMessageManager.dispose();
//    }
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

//    public void unsubscribe() {
//        if (mSubscription != null) {
//            mSubscription.cancelSubscription();
//            mChatMessageManager.dispose();
//        }
//    }
//
//    public void pauseSubscription() {
//        if (mSubscription != null) {
//            mSubscription.pauseSubscription();
//        }
//    }
//
//    public void cancelSubscription() {
//        if (mSubscription != null) {
//            mSubscription.cancelSubscription();
//        }
//    }
}
