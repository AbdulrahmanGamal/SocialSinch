package com.social.backendless.bus;

import com.social.backendless.model.ChatMessage;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Receives chat messages from the Views and send them to the ChatMessageHandler
 */
public class RxIncomingMessageBus {
    private static RxIncomingMessageBus mRxMessageBus;
    private final PublishSubject<ChatMessage> messageBus = PublishSubject.create();

    public static RxIncomingMessageBus getInstance() {
        if (mRxMessageBus == null) {
            mRxMessageBus = new RxIncomingMessageBus();
        }
        return mRxMessageBus;
    }

    public void sendMessage(ChatMessage message) {
        messageBus.onNext(message);
    }

    public Observable<ChatMessage> getMessageObservable() {
        return messageBus;
    }

    public boolean hasObservers() {
        return messageBus.hasObservers();
    }
}
