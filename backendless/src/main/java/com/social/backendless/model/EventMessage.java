package com.social.backendless.model;

/**
 * Model to encapsulate the Event Message received
 */

public class EventMessage {
    private String mPublisherId;
    private String mRecipientId;
    private String mEventMessage;
    private EventStatus mEventStatus;

    public EventMessage(String publisherId,
                        String recipientId,
                        String eventMessage,
                        EventStatus eventStatus) {
        this.mPublisherId = publisherId;
        this.mRecipientId = recipientId;
        this.mEventMessage = eventMessage;
        this.mEventStatus = eventStatus;
    }

    public String getEventMessage() {
        return mEventMessage;
    }

    public EventStatus getEventStatus() {
        return mEventStatus;
    }

    public String getPublisherId() {
        return mPublisherId;
    }

    public String getRecipientId() {
        return mRecipientId;
    }
}
