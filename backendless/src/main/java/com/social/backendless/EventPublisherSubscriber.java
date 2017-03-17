package com.social.backendless;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.Subscription;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.Message;
import com.backendless.messaging.PublishOptions;
import com.backendless.messaging.SubscriptionOptions;
import com.backendless.services.messaging.MessageStatus;
import com.social.backendless.model.EventStatus;
import com.social.backendless.utils.Constants;

import java.util.List;

import rx.Emitter;
import rx.Observable;
import rx.functions.Action1;

/**
 * Class to handle the connection and typing events
 */

public class EventPublisherSubscriber {

    private Subscription mSubscription;
    private static final String TAG = "EventPublisherSubscribe";
    private String mPublishedEventId;
    /**
     * Observable that publishes private messages to an specific recipient
     * @return
     */
    public void publishEvent(final EventStatus event, String recipientUserId) {
        PublishOptions publishOptions = new PublishOptions();
        publishOptions.setPublisherId(Backendless.UserService.loggedInUser());
        publishOptions.putHeader("recipient", recipientUserId);
        publishOptions.putHeader("messageType", "Event");
//        DeliveryOptions deliveryOptions = new DeliveryOptions();
//        deliveryOptions.setRepeatEvery( 60 ); // the message will be delivered every 10 seconds
        Backendless.Messaging.publish(Constants.DEFAULT_CHANNEL, event, publishOptions,
                new AsyncCallback<MessageStatus>() {
                    @Override
                    public void handleResponse(MessageStatus messageStatus) {
                      Log.e(TAG, "Event: " + event + " published successfully");
                      mPublishedEventId = messageStatus.getMessageId();
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Log.e(TAG, "Error sending Event");
                    }
                });
    }

    /**
     * Creates an emitter observable to listen event messages from the contacts
     */
    public Observable<Object> subscribeToGlobalEvents() {
        final SubscriptionOptions subscriptionOptions = new SubscriptionOptions();
        subscriptionOptions.setSubscriberId(Backendless.UserService.loggedInUser());
        subscriptionOptions.setSelector("recipient = '" + Backendless.UserService.loggedInUser() + "' AND messageType = 'Event'");
       return Observable.fromEmitter(new Action1<Emitter<Object>>() {
            @Override
            public void call(final Emitter<Object> emitter) {
                Backendless.Messaging.subscribe(Constants.DEFAULT_CHANNEL,
                        new AsyncCallback<List<Message>>() {
                            @Override
                            public void handleResponse( List<Message> response ) {
                                Log.e(TAG, "Received from Event Susbcription: " + response);
                                emitter.onNext(response);
                            }
                            @Override
                            public void handleFault( BackendlessFault fault ) {
                                Log.e(TAG, "Fault Receiving Event message : " + fault);
                                emitter.onNext(fault);
                            }
                        }, subscriptionOptions ,
                        new AsyncCallback<Subscription>() {
                            @Override
                            public void handleResponse( Subscription response )
                            {
                                mSubscription = response;
                            }
                            @Override
                            public void handleFault( BackendlessFault fault )
                            {
                                Log.e(TAG, "Subscription fault : " + fault);
                            }
                        }
                );
            }
        }, Emitter.BackpressureMode.NONE);

    }

    public void resumeSubscription() {
        if (mSubscription != null) {
            mSubscription.resumeSubscription();
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
        if (mPublishedEventId != null) {
            Backendless.Messaging.cancel(mPublishedEventId);
        }
    }
}
