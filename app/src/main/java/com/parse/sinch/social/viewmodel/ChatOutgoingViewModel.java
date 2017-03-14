package com.parse.sinch.social.viewmodel;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.social.backendless.model.ChatMessage;

/**
 * Created by valgood on 2/20/2017.
 */

public class ChatOutgoingViewModel {
    private ChatMessage mChatMessage;

    public ChatOutgoingViewModel(final ChatMessage chatMessage){
        this.mChatMessage = chatMessage;
    }

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView indicatorImgView, ChatOutgoingViewModel viewModel) {
        indicatorImgView.setImageResource(viewModel.getIconStatus());
    }
    public int getIconStatus() {
        return mChatMessage.getResourceId();
    }

    public ChatMessage getChatMessage() {
        return mChatMessage;
    }
}
