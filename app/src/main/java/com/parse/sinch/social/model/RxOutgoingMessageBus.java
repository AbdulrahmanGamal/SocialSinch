package com.parse.sinch.social.model;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by valgood on 2/21/2017.
 */

public class RxOutgoingMessageBus {
    private static RxOutgoingMessageBus mRxMessageBus;
    private final PublishSubject<ChatMessage> messageBus = PublishSubject.create();

    public static RxOutgoingMessageBus getInstance() {
        if (mRxMessageBus == null) {
            mRxMessageBus = new RxOutgoingMessageBus();
        }
        return mRxMessageBus;
    }

    public void setMessage(ChatMessage message) {
        messageBus.onNext(message);
    }

    public Observable<ChatMessage> getMessageObservable() {
        return messageBus;
    }

    public boolean hasObservers() {
        return messageBus.hasObservers();
    }
}
