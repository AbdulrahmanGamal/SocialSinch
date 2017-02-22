package com.parse.sinch.social.viewmodel;

import android.content.Context;

import com.parse.sinch.social.model.ChatInfo;

/**
 * Created by valgood on 2/20/2017.
 */

public class ChatItemListViewModel {
    private Context mContext;
    private ChatInfo mChatMessage;

    public ChatItemListViewModel(Context context, ChatInfo chatMessage){
        this.mContext = context;
        this.mChatMessage = chatMessage;
    }

    public ChatInfo getChatMessage() {
        return mChatMessage;
    }
}
