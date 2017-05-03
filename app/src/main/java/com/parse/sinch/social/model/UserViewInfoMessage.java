package com.parse.sinch.social.model;

import com.social.backendless.model.ChatMessage;
import com.social.backendless.model.ChatStatus;
import com.social.backendless.model.UserInfo;
import com.social.backendless.utils.DateUtils;

/**
 * Wrapper to encapsulate the user info and other information related with the list users screen
 */
public class UserViewInfoMessage {
    private UserInfo mUserInfo;
    private ViewMessage mLastViewMessage;

    public UserViewInfoMessage(UserInfo userInfo, ViewMessage viewMessage) {
        this.mUserInfo = userInfo;
        this.mLastViewMessage = viewMessage;
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public ViewMessage getLastViewMessage() {
        return mLastViewMessage;
    }

    public void setLastViewMessage(ViewMessage lastViewMessage) {
        //keep the text message delivered to avoid empty message shown only in delivered case
        if (lastViewMessage.getChatMessage().getStatus() != null &&
            lastViewMessage.getChatMessage().getStatus().equals(ChatStatus.DELIVERED)) {
            lastViewMessage.getChatMessage().setTextBody(mLastViewMessage.getChatMessage().getTextBody());
        }
        this.mLastViewMessage = lastViewMessage;
    }

    public void updateLastMessage(ChatMessage lastChatMessage) {
        mLastViewMessage.setChatMessage(lastChatMessage);
    }
}
