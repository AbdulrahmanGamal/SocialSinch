package com.parse.sinch.social.model;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by valgood on 2/21/2017.
 */

public class RxMessageBus {
    private static RxMessageBus mRxMessageBus;
    private final PublishSubject<ChatInfo> messageBus = PublishSubject.create();;

    public static RxMessageBus getInstance() {
        if (mRxMessageBus == null) {
            mRxMessageBus = new RxMessageBus();
        }
        return mRxMessageBus;
    }

    public void setMessage(ChatInfo message) {
        messageBus.onNext(message);
    }

    public Observable<ChatInfo> getMessageObservable() {
        return messageBus;
    }

    public boolean hasObservers() {
        return messageBus.hasObservers();
    }
}
