package com.vanniktech.emoji.custom;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiVariantPopup;
import com.vanniktech.emoji.EmojiView;
import com.vanniktech.emoji.RecentEmoji;
import com.vanniktech.emoji.RecentEmojiManager;
import com.vanniktech.emoji.listeners.OnEmojiLongClickedListener;
import com.vanniktech.emoji.R;
import com.vanniktech.emoji.Utils;
import com.vanniktech.emoji.gif.GIFGridView;
import com.vanniktech.emoji.listeners.OnEmojiClickedListener;
import com.vanniktech.emoji.listeners.OnScrollListener;

/**
 * Gif View that is shown inside the bottom pop up
 */

public class EmojiGIFView extends FrameLayout implements OnScrollListener {

    private ProgressBar mProgressBar;
    private Toolbar mToolbar;
    private int mToolbarHeight;
    private EmojiEditText mEmojiEditText;
    private RecentEmoji mRecentEmoji;
    private EmojiVariantPopup mVariantPopup;
    private PopupWindow mPopupWindow;
    private EmojiView mEmojiView;
    private GIFGridView mGifView;

    private static final String TAG = "EmojiGIFView";

    public EmojiGIFView(Context context, EmojiEditText emojiEditText) {
        super(context);
        this.mEmojiEditText = emojiEditText;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.general_layout_view, this, true);
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.emoji_background));
        mEmojiView = getEmojiView();
        addView(mEmojiView, 0);
        mGifView = new GIFGridView(getContext(), this);
        addView(mGifView, 1);
        mToolbar = (Toolbar) findViewById(R.id.toolbarBottomMenu);
        mToolbarHeight = Utils.getToolbarHeight(getContext());
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        initEmojiClickListener();
        initGifClickListener();
    }

    private void initEmojiClickListener() {
        findViewById(R.id.emoji_button).setOnClickListener(view -> {
            toggle();
        });
    }

    private void initGifClickListener() {
        findViewById(R.id.gif_button).setOnClickListener(view -> {
            toggle();
        });
    }

    public void toggle() {
        if (mGifView.isShown()) {
            mGifView.hideView();
            mEmojiView.showView();
        } else {
            mEmojiView.hideView();
            mGifView.showView();
        }
    }

    private EmojiView getEmojiView() {
        mRecentEmoji = new RecentEmojiManager(getContext());
        mPopupWindow = new PopupWindow(getContext());

        final OnEmojiLongClickedListener longClickListener = (view, emoji) -> mVariantPopup.show(view, emoji);

        final OnEmojiClickedListener clickListener = emoji -> {
            mEmojiEditText.input(emoji);
            mRecentEmoji.addEmoji(emoji);
            mVariantPopup.dismiss();
        };

        mVariantPopup = new EmojiVariantPopup(getRootView(), clickListener);

        final EmojiView emojiView = new EmojiView(getContext(), clickListener, longClickListener, mRecentEmoji);

        emojiView.setOnEmojiBackspaceClickListener(v -> mEmojiEditText.backspace());

        return emojiView;
    }
    @Override
    public void onScrollingUp() {
        mToolbar.animate().translationY(0).
                setInterpolator(new AccelerateInterpolator()).start();
    }

    @Override
    public void onScrollingDown() {
        mToolbar.animate().translationY(mToolbarHeight).
                setInterpolator(new AccelerateInterpolator()).start();
    }

    @Override
    public void onLoadingStarted() {
        mProgressBar.setVisibility(VISIBLE);
    }

    @Override
    public void onLoadFinished() {
        mProgressBar.setVisibility(INVISIBLE);
    }

    @Override
    protected void onDetachedFromWindow() {
        mPopupWindow.dismiss();
        mVariantPopup.dismiss();
        mRecentEmoji.persist();
        super.onDetachedFromWindow();
    }
}
