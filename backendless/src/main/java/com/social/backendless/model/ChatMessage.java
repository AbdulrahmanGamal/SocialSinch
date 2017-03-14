package com.social.backendless.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Chat Message model to store the information exchange between users
 */

public class ChatMessage implements Serializable{
    private Long mMessageId;
    private String mTextBody;
    private String mRecipientId;
    private String mSenderId;
    private Date mTimestamp;
    private ChatStatus mStatus;
    private String mSentId;
    private int mResourceId;

    public ChatStatus getStatus() {
        return mStatus;
    }

    public void setStatus(ChatStatus status) {
        this.mStatus = status;
    }

    public Long getMessageId() {
        return mMessageId;
    }

    public void setMessageId(Long mMessageId) {
        this.mMessageId = mMessageId;
    }

    public String getTextBody() {
        return mTextBody;
    }

    public void setTextBody(String mTextBody) {
        this.mTextBody = mTextBody;
    }

    public String getRecipientId() {
        return mRecipientId;
    }

    public void setRecipientIds(String recipientId) {
        this.mRecipientId = recipientId;
    }

    public String getSenderId() {
        return mSenderId;
    }

    public void setSenderId(String mSenderId) {
        this.mSenderId = mSenderId;
    }

    public Date getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(Date mTimestamp) {
        this.mTimestamp = mTimestamp;
    }

    public int getResourceId() {
        return mResourceId;
    }

    public void setResourceId(int resourceId) {
        this.mResourceId = resourceId;
    }

    public String getSentId() {
        return mSentId;
    }

    public void setSentId(String sentId) {
        this.mSentId = sentId;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "mMessageId=" + mMessageId +
                ", mTextBody='" + mTextBody + '\'' +
                ", mRecipientId=" + mRecipientId +
                ", mSenderId='" + mSenderId + '\'' +
                ", mTimestamp=" + mTimestamp +
                ", mStatus=" + mStatus +
                ", mSentId='" + mSentId + '\'' +
                ", mResourceId=" + mResourceId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatMessage that = (ChatMessage) o;

        return mMessageId.equals(that.mMessageId);

    }

    @Override
    public int hashCode() {
        return mMessageId.hashCode();
    }
}
