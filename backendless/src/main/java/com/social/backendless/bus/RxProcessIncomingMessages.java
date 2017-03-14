package com.social.backendless.bus;

import com.social.backendless.model.ChatMessage;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by valgood on 2/21/2017.
 */

public class RxProcessIncomingMessages {
    private static RxProcessIncomingMessages mRxMessageBus;
    private final PublishSubject<ChatMessage> messageBus = PublishSubject.create();

    public static RxProcessIncomingMessages getInstance() {
        if (mRxMessageBus == null) {
            mRxMessageBus = new RxProcessIncomingMessages();
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
