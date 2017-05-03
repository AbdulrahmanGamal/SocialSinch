package com.parse.sinch.social.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.parse.sinch.social.R;

/**
 * Created by valgood on 3/26/2017.
 */

public class ProfileHeaderView extends RelativeLayout {

    private TextView mName;
    private TextView mLastSeen;


    public ProfileHeaderView(Context context) {
        super(context);
        init();
    }

    public ProfileHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProfileHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProfileHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        init(LayoutInflater.from(getContext()).inflate(R.layout.profile_header_view, this, true));
    }

    private void init(View v) {
        mName = (TextView) v.findViewById(R.id.header_view_title);
        mLastSeen = (TextView) v.findViewById(R.id.header_view_sub_title);
    }

    public void bindTo(String title, String subTitle) {
        hideOrSetText(this.mName, title);
        hideOrSetText(this.mLastSeen, subTitle);
    }

    private void hideOrSetText(TextView tv, String text) {
        if (text == null || text.equals(""))
            tv.setVisibility(GONE);
        else
            tv.setText(text);
    }

    public void setScaleXTitle(float scaleXTitle) {
        mName.setScaleX(scaleXTitle);
        mName.setPivotX(0);
    }

    public void setScaleYTitle(float scaleYTitle) {
        mName.setScaleY(scaleYTitle);
        mName.setPivotY(30);
    }

    public TextView getName() {
        return mName;
    }

    public TextView getLastSeen() {
        return mLastSeen;
    }
}
