package com.social.backendless.data;

import android.util.Log;

import com.backendless.messaging.Message;
import com.backendless.services.messaging.MessageStatus;
import com.google.gson.Gson;
import com.social.backendless.bus.RxIncomingEventBus;
import com.social.backendless.bus.RxOutgoingEventBus;
import com.social.backendless.model.EventMessage;
import com.social.backendless.utils.Constants;
import com.social.backendless.utils.ObservableUtils;

import io.reactivex.functions.Consumer;
import rx.functions.Action1;

/**
 * Manager to control all the envets received/sent in the app
 */

public class EventMessageManager {
    private static final String TAG = "EventMessageManager";

    public EventMessageManager() {
       configureIncomingEvents();
    }
    /**
     * Start listening for events coming from the UI
     */
    private void configureIncomingEvents() {
        RxIncomingEventBus.getInstance().getMessageObservable().subscribe(new Consumer<EventMessage>() {
            @Override
            public void accept(EventMessage eventMessage) throws Exception {
                Log.e(TAG, "Received event to publish");
                sendEvent(eventMessage);
            }
        });
    }
    /**
     * Convert the message into an EventMEssage and sent it though the bus
     * @param event
     */
    public void processIncomingEvent(Message event) {
        final Gson gson = new Gson();
        EventMessage eventMessage = gson.fromJson((String) event.getData(), EventMessage.class);
        RxOutgoingEventBus.getInstance().setEvent(eventMessage);
    }

    /**
     * Sends an instant message to the default channel
     * @param message
     */
    private void sendEvent(EventMessage message) {
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(message);

        ObservableUtils.getPublisher(jsonMessage,
                message.getPublisherId(),
                message.getRecipientId(),
                Constants.MESSAGE_TYPE_EVENT_KEY).subscribe(new Action1<Object>() {
            @Override
            public void call(Object result) {
                if (result instanceof MessageStatus) {
                    Log.e(TAG, "Event was published " + result);
                } else {
                    Log.e(TAG, "Event was not published: " + result);
                }
            }
        });
    }
}
