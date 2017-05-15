package com.social.valgoodchat.viewmodel;

import android.databinding.BindingAdapter;
import android.databinding.adapters.FrameLayoutBindingAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.social.backendless.utils.DateUtils;
import com.social.valgoodchat.R;
import com.social.valgoodchat.model.ViewMessage;
import com.vanniktech.emoji.EmojiTextView;
import com.vanniktech.emoji.emoji.Emoji;

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

    @BindingAdapter("android:text")
    public static void setTextBody(EmojiTextView editText, ChatOutgoingViewModel viewModel) {
        String message = viewModel.getViewChatMessage().getChatMessage().getTextBody();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) editText.getLayoutParams();
        if (message.length() <= 15) {
            int paddingRight =
                    editText.getResources().getDimensionPixelOffset(R.dimen.outgoing_message_padding_right_few_chars);
            params.setMargins(params.leftMargin, params.topMargin, paddingRight, 0);
            switch (message.length()) {
                case 2:
                    editText.setEmojiSize(editText.getResources().getDimensionPixelSize(R.dimen.emoji_size_message_three));
                    break;
                case 1:
                    //heart emoji is the biggest
                    if (message.charAt(0) == (char)10084) {
                        editText.setEmojiSize(editText.getResources().
                                getDimensionPixelSize(R.dimen.emoji_heart_size_message));
                    }
                    break;
                default:
                    editText.setEmojiSize(editText.getResources().getDimensionPixelSize(R.dimen.emoji_size_message_two));
                    break;
            }
        } else {
            int defaultTop =
                    editText.getResources().getDimensionPixelOffset(R.dimen.outgoing_message_default_padding_top);
            int defaultBottom =
                    editText.getResources().getDimensionPixelOffset(R.dimen.outgoing_message_default_padding_bottom);
            int defaultLeft =
                    editText.getResources().getDimensionPixelOffset(R.dimen.outgoing_message_default_padding_left);
            int defaultRight =
                    editText.getResources().getDimensionPixelOffset(R.dimen.outgoing_message_default_padding_right);
            params.setMargins(defaultLeft, defaultTop, defaultRight, defaultBottom);
            editText.setEmojiSize(editText.getResources().getDimensionPixelSize(R.dimen.emoji_size_message));
        }
        editText.requestLayout();
        editText.setText(message);
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
