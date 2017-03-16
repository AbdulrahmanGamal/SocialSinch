package com.parse.sinch.social.viewmodel;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.parse.sinch.social.model.ViewMessage;

/**
 * Created by valgood on 2/20/2017.
 */

public class ChatIncomingViewModel {
    private Context mContext;
    private ViewMessage mViewChatMessage;
    private Drawable mIconStatus;

    public ChatIncomingViewModel(Context context, ViewMessage viewChatMessage){
        this.mContext = context;
        this.mViewChatMessage = viewChatMessage;
    }

    @BindingAdapter("indicator")
    public static void setIconStatus(ImageView indicatorImgView, ChatIncomingViewModel viewModel) {
        indicatorImgView.setImageDrawable(viewModel.getIconStatus());
    }
    public Drawable getIconStatus() {
        return mIconStatus;
    }

    public ViewMessage getViewChatMessage() {
        return mViewChatMessage;
    }

}
