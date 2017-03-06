package com.parse.sinch.social.viewmodel;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.parse.sinch.social.model.UserInfo;

/**
 * Created by valgood on 2/27/2017.
 */

public class ToolbarChatViewModel {
    private Context mContext;
    private UserInfo mUserInfo;

    public ToolbarChatViewModel(Context context, UserInfo userInfo) {
        this.mContext = context;
        this.mUserInfo = userInfo;
    }

    public String getUserName() {
        return mUserInfo.getFullName();
    }
    public String getLastSeen() {
        return "one hour ago";
    }


    @BindingAdapter("avatarUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).load(url).into(imageView);
    }
    public String getUserAvatar() {
        return mUserInfo.getProfilePicture();
    }
}
