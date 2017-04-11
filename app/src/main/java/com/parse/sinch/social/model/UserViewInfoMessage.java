package com.parse.sinch.social.model;

import com.social.backendless.model.ChatMessage;
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
        this.mLastViewMessage = lastViewMessage;
    }

    public void updateLastMessage(ChatMessage lastChatMessage) {
        mLastViewMessage.setChatMessage(lastChatMessage);
    }
}
