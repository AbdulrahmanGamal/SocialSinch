package com.social.backendless.bus;
import com.social.backendless.model.EventMessage;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Bus to Receive global events that new to be sent to specific recipients from the views
 */

public class RxIncomingEventBus {
    private static RxIncomingEventBus mRxMessageBus;
    private final PublishSubject<EventMessage> messageBus = PublishSubject.create();

    public static RxIncomingEventBus getInstance() {
        if (mRxMessageBus == null) {
            mRxMessageBus = new RxIncomingEventBus();
        }
        return mRxMessageBus;
    }

    public void sendEvent(EventMessage event) {
        messageBus.onNext(event);
    }

    public Observable<EventMessage> getMessageObservable() {
        return messageBus;
    }

    public boolean hasObservers() {
        return messageBus.hasObservers();
    }


}
