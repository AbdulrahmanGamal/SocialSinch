package com.social.backendless.utils;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.PublishOptions;
import com.backendless.services.messaging.MessageStatus;

import rx.Emitter;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by jorgevalbuena on 3/17/17.
 */

public class ObservableUtils {

    /**
     * Observable that publishes private messages to an specific recipient
     * @return
     */
    public static Observable<Object> getPublisher(final String jsonMessage,
                                                   final String publisherId,
                                                   final String recipientId,
                                                   final String messageType) {
        return Observable.fromEmitter(new Action1<Emitter<Object>>() {
            @Override
            public void call(final Emitter<Object> emitter) {
                PublishOptions publishOptions = new PublishOptions();
                publishOptions.setPublisherId(publisherId);
                publishOptions.putHeader(Constants.RECIPIENT_KEY, recipientId);
                publishOptions.putHeader(Constants.MESSAGE_TYPE_KEY, messageType);
                Backendless.Messaging.publish(Constants.DEFAULT_CHANNEL, jsonMessage, publishOptions, new AsyncCallback<MessageStatus>() {
                    @Override
                    public void handleResponse(MessageStatus messageStatus) {
                        emitter.onNext(messageStatus);
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        emitter.onNext(backendlessFault);
                    }
                });
            }
        }, Emitter.BackpressureMode.BUFFER).share();
    }
}
