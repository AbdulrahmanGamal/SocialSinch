package com.parse.sinch.social.model;

/**
 * Created by valgood on 2/20/2017.
 */

public class ChatInfo {
    private long mChatId;
    private String mMessage;
    private String mDate;
    private ChatStatus mStatus;


    public enum ChatStatus {
        NOT_SENT,
        SENT,
        DELIVERED,
        RECEIVED
    }

    public long getChatId() {
        return mChatId;
    }

    public void setChatId(long chatId) {
        this.mChatId = chatId;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public ChatStatus getStatus() {
        return mStatus;
    }

    public void setStatus(ChatStatus status) {
        this.mStatus = status;
    }
}
