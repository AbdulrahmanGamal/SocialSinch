package com.parse.sinch.social.viewmodel;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.sinch.social.model.ViewMessage;
import com.social.backendless.utils.DateUtils;

/**
 * Created by valgood on 2/20/2017.
 */

public class ChatIncomingViewModel {
    private ViewMessage mViewChatMessage;

    public ChatIncomingViewModel(Context context, ViewMessage viewChatMessage){
        this.mViewChatMessage = viewChatMessage;
    }

    public String getFormattedTime() {
        return DateUtils.convertChatDate(mViewChatMessage.getChatMessage().getTimestamp());
    }

    public ViewMessage getViewChatMessage() {
        return mViewChatMessage;
    }

}
