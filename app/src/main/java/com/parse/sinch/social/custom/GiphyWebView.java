package com.parse.sinch.social.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.parse.sinch.social.R;

/**
 * Custom WebView to display GIF
 */

public class GiphyWebView extends LinearLayout {
    private ImageView mGifView;


    public GiphyWebView(Context context) {
        super(context);
        init();
    }

    public GiphyWebView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GiphyWebView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GiphyWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        init(LayoutInflater.from(getContext()).inflate(R.layout.web_view_custom, this, true));
    }

    private void init(View v) {
        mGifView = (ImageView) v.findViewById(R.id.gifView);
    }

    public void bindTo(String gifName) {
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(mGifView);
        Glide.with(mGifView.getContext()).load(gifName).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageViewTarget);
    }
}
