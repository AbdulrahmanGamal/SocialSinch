package com.social.valgoodchat.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.social.backendless.model.ChatStatus;
import com.social.backendless.utils.DateUtils;
import com.social.valgoodchat.MessagesActivity;
import com.social.valgoodchat.model.UserViewInfoMessage;
import com.social.valgoodchat.utils.Constants;
import com.social.valgoodchat.utils.ImageLoading;
import com.social.valgoodchat.utils.Utils;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * View Model to populate the user calls information in the calls recycler view
 */
public class UserCallsItemViewModel extends BaseObservable {
    private Context mContext;
    private UserViewInfoMessage mUserInfo;

    public UserCallsItemViewModel(Context context, UserViewInfoMessage userInfo) {
        this.mContext = context;
        this.mUserInfo = userInfo;
    }

    public String getFullName() {
       return mUserInfo.getUserInfo().getFullName();
    }

    public String getLastMessage() {
        return mUserInfo.getLastViewMessage().getChatMessage().getTextBody();
    }

    public String getLastMessageDate() {
        return DateUtils.
                convertChatDate(mUserInfo.getLastViewMessage().getChatMessage().getTimestamp());
    }
    @BindingAdapter("android:src")
    public static void setImageResource(ImageView indicatorImgView, UserCallsItemViewModel model) {
        model.setMessageIndicator(indicatorImgView);
    }

    @BindingAdapter("avatarUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        ImageLoading.loadContactPicture(url, imageView);
    }
    public String getUserAvatar() {
        return mUserInfo.getUserInfo().getProfilePicture();
    }

    public View.OnClickListener onClickUserCall() {
        return v -> {
            Log.e("UserCallsViewModel", "Click on user call");
            Intent intent = new Intent(mContext, MessagesActivity.class);
            intent.putExtra(Constants.RECIPIENT_ID, mUserInfo.getUserInfo().getObjectId());
            mContext.startActivity(intent);
        };
    }

    /**
     * Based on the last message status, set up the indicator icon
     * @param view
     */
    public void setMessageIndicator(ImageView view) {
        if (mUserInfo.getLastViewMessage() == null ||
            mUserInfo.getLastViewMessage().getChatMessage().getMessageId() == null ||
            mUserInfo.getLastViewMessage().getChatMessage().getStatus() == null ||
            mUserInfo.getLastViewMessage().getChatMessage().getStatus().equals(ChatStatus.RECEIVED)) {
            view.setVisibility(View.GONE);
            return;
        }

        Utils.changeStatusIcon(mUserInfo.getLastViewMessage(),
                mUserInfo.getLastViewMessage().getChatMessage().getStatus());
        view.setImageResource(mUserInfo.getLastViewMessage().getResourceId());
        view.setVisibility(View.VISIBLE);
    }
}