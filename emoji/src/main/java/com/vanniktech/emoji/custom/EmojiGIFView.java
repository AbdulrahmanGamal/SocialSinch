package com.vanniktech.emoji.custom;

import android.annotation.SuppressLint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.R;

/**
 * Gif View that is shown inside the bottom pop up
 */
@SuppressLint("ViewConstructor")
public class EmojiGIFView extends FrameLayout {

    public EmojiGIFView(View rootView, EmojiEditText emojiEditText) {
        super(rootView.getContext());
        init(rootView, emojiEditText);
    }

    private void init(View rootView, EmojiEditText emojiEditText) {
        LayoutInflater.from(getContext()).inflate(R.layout.general_layout_view, this, true);
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.emoji_background));

        //index 0 add the emoji view visible
        Toolbar bottomToolbar = (Toolbar) findViewById(R.id.toolbarBottomMenu);
        addView(new NewEmojiView(rootView, emojiEditText, bottomToolbar), 0);
        //index 1 add the gif grid view invisible
        AnimatedEmoticonView gifGridView = new AnimatedEmoticonView(getContext(), bottomToolbar);
        gifGridView.hideView();
        addView(gifGridView, 1);

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
        //based on the dynamically addition done in init, the position indexes must always match
        BasicEmoticonView emojiView = (BasicEmoticonView) getChildAt(0);
        BasicEmoticonView gifGridView = (BasicEmoticonView) getChildAt(1);
        if (gifGridView.isShown()) {
            gifGridView.hideView();
            emojiView.showView();
        } else {
            emojiView.hideView();
            gifGridView.showView();
        }
    }

//    @Override
//    public void onLoadingStarted() {
//        findViewById(R.id.progressBar).setVisibility(VISIBLE);
//    }
//
//    @Override
//    public void onLoadFinished() {
//        findViewById(R.id.progressBar).setVisibility(INVISIBLE);
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        mVariantPopup.dismiss();
//        mRecentEmoji.persist();
//        super.onDetachedFromWindow();
//    }
}
