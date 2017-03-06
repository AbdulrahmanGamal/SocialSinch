package com.parse.sinch.social.model;

import java.util.Date;
import java.util.List;

/**
 * Chat Message model to store the information exchange between users
 */

public class ChatMessage {
    private Long mMessageId;
    private String mTextBody;
    private List<String> mRecipientIds;
    private String mSenderId;
    private Date mTimestamp;
    private ChatStatus mStatus;
    private String mSentId;
    private int mResourceId;


    public enum ChatStatus {
        FAILED,
        WAITING,
        SENT,
        DELIVERED,
        RECEIVED
    }

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

    public List<String> getRecipientIds() {
        return mRecipientIds;
    }

    public void setRecipientIds(List<String> mRecipientIds) {
        this.mRecipientIds = mRecipientIds;
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
                ", mRecipientIds=" + mRecipientIds +
                ", mSenderId='" + mSenderId + '\'' +
                ", mTimestamp=" + mTimestamp +
                ", mStatus=" + mStatus +
                ", mSentId='" + mSentId + '\'' +
                ", mResourceId=" + mResourceId +
                '}';
    }
}
