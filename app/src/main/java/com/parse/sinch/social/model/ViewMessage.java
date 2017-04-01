package com.parse.sinch.social.model;

import com.social.backendless.model.ChatMessage;

/**
 * Message model class used in the UI to hold the different veriables to control a chat message
 */

public class ViewMessage {
    private Long mViewMessageId;
    private int mResourceId;
    private ChatMessage mChatMessage;

    public ViewMessage(ChatMessage chatMessage) {
        this.mChatMessage = chatMessage;
    }

    public Long getViewMessageId() {
        return mViewMessageId;
    }

    public void setViewMessageId(Long viewMessageId) {
        this.mViewMessageId = viewMessageId;
    }

    public int getResourceId() {
        return mResourceId;
    }

    public void setResourceId(int resourceId) {
        this.mResourceId = resourceId;
    }

    public ChatMessage getChatMessage() {
        return mChatMessage;
    }
}
