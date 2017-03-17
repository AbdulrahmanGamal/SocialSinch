package com.social.backendless.bus;
import com.social.backendless.model.EventStatus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Bus to transfer global events to all the views
 */

public class RxGlobalEventBus {
    private static RxGlobalEventBus mRxMessageBus;
    private final PublishSubject<EventStatus> messageBus = PublishSubject.create();

    public static RxGlobalEventBus getInstance() {
        if (mRxMessageBus == null) {
            mRxMessageBus = new RxGlobalEventBus();
        }
        return mRxMessageBus;
    }

    public void setEvent(EventStatus event) {
        messageBus.onNext(event);
    }

    public Observable<EventStatus> getMessageObservable() {
        return messageBus;
    }

    public boolean hasObservers() {
        return messageBus.hasObservers();
    }


}
