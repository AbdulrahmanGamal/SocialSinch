package com.parse.sinch.social.viewmodel;

import android.databinding.BindingAdapter;
import android.widget.FrameLayout;

import com.parse.sinch.social.R;
import com.parse.sinch.social.model.ViewMessage;
import com.social.backendless.utils.DateUtils;

/**
 * View Model to display the message incoming
 */

public class ChatIncomingViewModel {
    private ViewMessage mViewChatMessage;

    public ChatIncomingViewModel(ViewMessage viewChatMessage){
        this.mViewChatMessage = viewChatMessage;
    }

    public String getFormattedTime() {
        return DateUtils.convertChatDate(mViewChatMessage.getChatMessage().getTimestamp());
    }

    public ViewMessage getViewChatMessage() {
        return mViewChatMessage;
    }

    @BindingAdapter("android:background")
    public static void setMessageBackground(FrameLayout layout, ChatIncomingViewModel viewModel) {
        layout.setBackgroundResource(viewModel.getBackground());
    }

    public int getBackground() {
        if (mViewChatMessage.isFirstMessage()) {
            return R.drawable.balloon_incoming_normal_ext;
        } else {
            return R.drawable.balloon_incoming_normal;
        }
    }
}
