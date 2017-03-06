package com.parse.sinch.social.model;

/**
 * Class to store the current conversations and the amount of messages exchanged
 */

public class Conversation {
    private String mId;
    private int mTotalMessages;

    public Conversation(String id, int totalMessages) {
        this.mId = id;
        this.mTotalMessages = totalMessages;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public int getTotalMessages() {
        return mTotalMessages;
    }

    public void setTotalMessages(int totalMessages) {
        this.mTotalMessages = totalMessages;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "mId='" + mId + '\'' +
                ", mTotalMessages=" + mTotalMessages +
                '}';
    }
}
