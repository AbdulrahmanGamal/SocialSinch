package com.parse.sinch.social.viewmodel;

import android.databinding.BindingAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.parse.sinch.social.R;
import com.parse.sinch.social.model.ViewMessage;
import com.social.backendless.utils.DateUtils;

/**
 * View Model class to bind the chat messages sent by the logged user
 */

public class ChatOutgoingViewModel {
    private ViewMessage mViewChatMessage;

    public ChatOutgoingViewModel(final ViewMessage chatMessage){
        this.mViewChatMessage = chatMessage;
    }

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView indicatorImgView, ChatOutgoingViewModel viewModel) {
        indicatorImgView.setImageResource(viewModel.getIconStatus());
    }

    @BindingAdapter("android:background")
    public static void setMessageBackground(FrameLayout layout, ChatOutgoingViewModel viewModel) {
        layout.setBackgroundResource(viewModel.getBackground());
    }

    public int getIconStatus() {
        return mViewChatMessage.getResourceId();
    }

    public ViewMessage getViewChatMessage() {
        return mViewChatMessage;
    }

    public String getFormattedTime() {
        return DateUtils.convertChatDate(mViewChatMessage.getChatMessage().getTimestamp());
    }

    public int getBackground() {
        if (mViewChatMessage.isFirstMessage()) {
            return R.drawable.balloon_outgoing_normal_ext;
        } else {
            return R.drawable.balloon_outgoing_normal;
        }
    }
}
