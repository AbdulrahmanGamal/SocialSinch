package com.vanniktech.emoji.custom;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPagerAdapter;
import com.vanniktech.emoji.EmojiVariantPopup;
import com.vanniktech.emoji.R;
import com.vanniktech.emoji.RecentEmoji;
import com.vanniktech.emoji.RecentEmojiManager;
import com.vanniktech.emoji.emoji.EmojiCategory;
import com.vanniktech.emoji.listeners.OnEmojiBackspaceClickListener;
import com.vanniktech.emoji.listeners.OnEmojiClickedListener;
import com.vanniktech.emoji.listeners.OnEmojiLongClickedListener;
import com.vanniktech.emoji.listeners.RepeatListener;

import java.util.concurrent.TimeUnit;

/**
 * Specific Implementation for static Emojis
 */
@SuppressLint("ViewConstructor")
public class NewEmojiView extends BasicEmoticonView {
    private final EmojiPagerAdapter mBasicPagerAdapter;
    private final RecentEmoji mRecentEmoji;
    private EmojiVariantPopup mVariantPopup;

    private OnEmojiBackspaceClickListener onEmojiBackspaceClickListener;
    private OnEmojiClickedListener onEmojiClickedListener;
    private OnEmojiLongClickedListener onEmojiLongClickedListener;

    private static final long INITIAL_INTERVAL = TimeUnit.SECONDS.toMillis(1) / 2;
    private static final int NORMAL_INTERVAL = 50;

    public NewEmojiView(View rootView, EmojiEditText emojiEditText, Toolbar toolbarBottom) {
        super(rootView.getContext());
        mRecentEmoji = new RecentEmojiManager(rootView.getContext());
        final LinearLayout emoticonTab = (LinearLayout) findViewById(R.id.emojis_tab);
        final EmojiCategory[] categories = EmojiManager.getInstance().getCategories();

        mEmoticonTabs = new ImageButton[categories.length + 2];
        mEmoticonTabs[0] = inflateButton(emoticonTab.getContext(), R.drawable.emoji_recent, emoticonTab);
        for (int i = 0; i < categories.length; i++) {
            mEmoticonTabs[i + 1] = inflateButton(emoticonTab.getContext(), categories[i].getIcon(), emoticonTab);
        }
        mEmoticonTabs[mEmoticonTabs.length - 1] = inflateButton(emoticonTab.getContext(), R.drawable.emoji_backspace, emoticonTab);
        configureListener(rootView, emojiEditText);
        mBasicPagerAdapter = new EmojiPagerAdapter(onEmojiClickedListener, onEmojiLongClickedListener,
                                                   mRecentEmoji, toolbarBottom);

        final ViewPager emoticonPager = (ViewPager) findViewById(R.id.emojis_pager);
        emoticonPager.addOnPageChangeListener(this);
        emoticonPager.setAdapter(mBasicPagerAdapter);
        handleOnClicks(emoticonPager);
        mEmoticonTabs[mEmoticonTabs.length - 1].setOnTouchListener(
                new RepeatListener(INITIAL_INTERVAL, NORMAL_INTERVAL, view -> {
                    if (onEmojiBackspaceClickListener != null) {
                        onEmojiBackspaceClickListener.onEmojiBackspaceClicked(view);
                    }
                }));

        final int startIndex = mBasicPagerAdapter.numberOfRecentEmojis() > 0 ? 0 : 1;
        emoticonPager.setCurrentItem(startIndex);
        onPageSelected(startIndex);
    }

    private void configureListener(View rootView, EmojiEditText emojiEditText) {
        onEmojiLongClickedListener = (view, emoji) -> mVariantPopup.show(view, emoji);

        onEmojiClickedListener= emoji -> {
            emojiEditText.input(emoji);
            mRecentEmoji.addEmoji(emoji);
            mVariantPopup.dismiss();
        };

        mVariantPopup = new EmojiVariantPopup(rootView, onEmojiClickedListener);
        setOnEmojiBackspaceClickListener(v -> emojiEditText.backspace());
    }

    public void setOnEmojiBackspaceClickListener(@Nullable final OnEmojiBackspaceClickListener onEmojiBackspaceClickListener) {
        this.onEmojiBackspaceClickListener = onEmojiBackspaceClickListener;
    }

    @Override
    protected void handleOnClicks(final ViewPager emojisPager) {
        super.handleOnClicks(emojisPager);
        mEmoticonTabs[mEmoticonTabs.length - 1].setOnTouchListener(
                new RepeatListener(INITIAL_INTERVAL, NORMAL_INTERVAL, view -> {
                    if (onEmojiBackspaceClickListener != null) {
                        onEmojiBackspaceClickListener.onEmojiBackspaceClicked(view);
                    }
                }));
    }

    @Override
    public void onPageSelected(final int i) {
        if (mEmoticonTabLastSelectedIndex != i) {
            if (i == 0) {
                mBasicPagerAdapter.invalidateRecentEmojis();
            }
        }
        super.onPageSelected(i);
    }

    @Override
    protected void onDetachedFromWindow() {
        mVariantPopup.dismiss();
        mRecentEmoji.persist();
        super.onDetachedFromWindow();
    }
}
