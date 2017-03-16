package com.social.backendless.model;

/**
 * ACK MEssage send to the originator of the message to let it know the message
 * reached its destination.
 */

public class ACKMessage {
    private String messageId;
    private String recipientId;

    public ACKMessage(String messageId, String recipientId) {
        this.messageId = messageId;
        this.recipientId = recipientId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }
}
