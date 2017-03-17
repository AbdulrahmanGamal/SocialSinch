package com.social.backendless.bus;
import com.social.backendless.model.EventMessage;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Bus to transfer global events to all the views
 */

public class RxOutgoingEventBus {
    private static RxOutgoingEventBus mRxMessageBus;
    private final PublishSubject<EventMessage> messageBus = PublishSubject.create();

    public static RxOutgoingEventBus getInstance() {
        if (mRxMessageBus == null) {
            mRxMessageBus = new RxOutgoingEventBus();
        }
        return mRxMessageBus;
    }

    public void setEvent(EventMessage event) {
        messageBus.onNext(event);
    }

    public Observable<EventMessage> getMessageObservable() {
        return messageBus;
    }

    public boolean hasObservers() {
        return messageBus.hasObservers();
    }


}
