package com.social.backendless.model;

import java.util.List;

/**
 * Wrapper with the list of current chats
 */

public class CurrentChatsEvent {
    private OperationResponse mResponse;
    private List<UserInfo> mCharUserInfo;

    public CurrentChatsEvent(OperationResponse operationResponse) {
        this.mResponse = operationResponse;
    }

    public String getCode() {
        return mResponse.getOpCode();
    }

    public String getError() {
        return mResponse.getError();
    }

    public List<UserInfo> getCharUserInfo() {
        return mCharUserInfo;
    }

    public void setCharUserInfo(List<UserInfo> charUserInfo) {
        this.mCharUserInfo = charUserInfo;
    }
}
