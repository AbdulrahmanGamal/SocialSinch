package com.parse.sinch.social.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.parse.sinch.social.R;

/**
 * Custom WebView to display GIF
 */

public class GiphyWebView extends LinearLayout {
    private Context context;
    private WebView mWebView;


    public GiphyWebView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public GiphyWebView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public GiphyWebView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GiphyWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    private void init() {
        init(LayoutInflater.from(getContext()).inflate(R.layout.web_view_custom, this, true));
    }

    private void init(View v) {
        mWebView = (WebView) v.findViewById(R.id.webView);
    }

    public void bindTo(String gifName) {
        String htmlGif = "<html style=\"margin: 0;\">\n" +
                "    <body style=\"margin: 0;\">\n" +
                "    <img src=" + gifName + " style=\"width: 100%; height: 100%\" />\n" +
                "    </body>\n" +
                "    </html>";

        mWebView.loadData(htmlGif, "text/html; charset=utf-8", "UTF-8");
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }
}
