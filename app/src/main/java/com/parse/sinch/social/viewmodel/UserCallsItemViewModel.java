package com.parse.sinch.social.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.parse.sinch.social.MessagesActivity;
import com.social.backendless.model.UserInfo;
import com.parse.sinch.social.utils.Constants;

/**
 * View Model to populate the user calls information in the calls recycler view
 */
public class UserCallsItemViewModel extends BaseObservable {
    private Context mContext;
    private UserInfo mUserInfo;

    public UserCallsItemViewModel(Context context, UserInfo userInfo) {
        this.mContext = context;
        this.mUserInfo = userInfo;
    }

    public String getFullName() {
       return mUserInfo.getFullName();
    }

    @BindingAdapter("avatarUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Uri imageUri = Uri.parse(url);
        imageView.setImageURI(imageUri);
    }
    public String getUserAvatar() {
        return mUserInfo.getProfilePicture();
    }

    public String getPhoneNumber() {
        return mUserInfo.getPhoneNumber();
    }

    public boolean isConnected() {
        return mUserInfo.isStatus();
    }

    public View.OnClickListener onClickUserCall() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("UserCallsViewModel", "Click on user call");
                Intent intent = new Intent(mContext, MessagesActivity.class);
                intent.putExtra(Constants.RECIPIENT_ID, mUserInfo.getObjectId());
                mContext.startActivity(intent);
            }
        };
    }
}
