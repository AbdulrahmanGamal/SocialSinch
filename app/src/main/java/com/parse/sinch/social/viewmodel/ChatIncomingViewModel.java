package com.parse.sinch.social.viewmodel;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.social.backendless.model.ChatMessage;

/**
 * Created by valgood on 2/20/2017.
 */

public class ChatIncomingViewModel {
    private Context mContext;
    private ChatMessage mChatMessage;
    private Drawable mIconStatus;

    public ChatIncomingViewModel(Context context, ChatMessage chatMessage){
        this.mContext = context;
        this.mChatMessage = chatMessage;
    }

    @BindingAdapter("indicator")
    public static void setIconStatus(ImageView indicatorImgView, ChatIncomingViewModel viewModel) {
        indicatorImgView.setImageDrawable(viewModel.getIconStatus());
    }
    public Drawable getIconStatus() {
        return mIconStatus;
    }

    public ChatMessage getChatMessage() {
        return mChatMessage;
    }

}
