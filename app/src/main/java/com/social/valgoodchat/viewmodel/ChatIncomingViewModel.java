package com.social.valgoodchat.viewmodel;

import android.databinding.BindingAdapter;
import android.widget.FrameLayout;

import com.social.backendless.utils.DateUtils;
import com.social.valgoodchat.R;
import com.social.valgoodchat.model.ViewMessage;
import com.social.valgoodchat.utils.Utils;
import com.vanniktech.emoji.EmojiTextView;

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

    @BindingAdapter("android:text")
    public static void setTextBody(EmojiTextView editText, ChatIncomingViewModel viewModel) {
        String message = viewModel.getViewChatMessage().getChatMessage().getTextBody();
        Utils.formatChatMessage(editText, message, true);
    }
    public int getBackground() {
        if (mViewChatMessage.isFirstMessage()) {
            return R.drawable.balloon_incoming_normal_ext;
        } else {
            return R.drawable.balloon_incoming_normal;
        }
    }
}
