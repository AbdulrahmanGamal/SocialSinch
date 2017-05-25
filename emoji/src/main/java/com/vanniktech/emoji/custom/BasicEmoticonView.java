package com.vanniktech.emoji.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.content.res.AppCompatResources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.vanniktech.emoji.R;
import com.vanniktech.emoji.listeners.EmoticonTabsClickListener;

/**
 * Generic emoticon template view to encapsulate common methods between Emojis and Aminated emoticons
 */
@SuppressLint("ViewConstructor")
public class BasicEmoticonView extends LinearLayout implements ViewPager.OnPageChangeListener {
    @ColorInt
    private final int mThemeAccentColor;
    @ColorInt
    private final int mThemeIconColor;
    protected ImageButton[] mEmoticonTabs;
    protected int mEmoticonTabLastSelectedIndex = -1;

    public BasicEmoticonView(Context context) {
        super(context);
        View.inflate(context, R.layout.emoji_view, this);
        setOrientation(VERTICAL);
        setBackgroundColor(ContextCompat.getColor(context, R.color.emoji_background));

        mThemeIconColor = ContextCompat.getColor(context, R.color.emoji_icons);
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        mThemeAccentColor = value.data;
    }

    protected void handleOnClicks(final ViewPager emojisPager) {
        for (int i = 0; i < mEmoticonTabs.length; i++) {
            mEmoticonTabs[i].setOnClickListener(new EmoticonTabsClickListener(emojisPager, i));
        }
    }

    protected ImageButton inflateButton(final Context context, @DrawableRes final int icon, final ViewGroup parent) {
        final ImageButton button = (ImageButton) LayoutInflater.from(context).inflate(R.layout.emoji_category, parent, false);

        button.setImageDrawable(AppCompatResources.getDrawable(context, icon));
        button.setColorFilter(mThemeIconColor, PorterDuff.Mode.SRC_IN);

        parent.addView(button);

        return button;
    }

    @Override
    public void onPageSelected(final int i) {
        if (mEmoticonTabLastSelectedIndex != i) {
            if (mEmoticonTabLastSelectedIndex >= 0 && mEmoticonTabLastSelectedIndex < mEmoticonTabs.length) {
                mEmoticonTabs[mEmoticonTabLastSelectedIndex].setSelected(false);
                mEmoticonTabs[mEmoticonTabLastSelectedIndex].setColorFilter(mThemeIconColor, PorterDuff.Mode.SRC_IN);
            }

            mEmoticonTabs[i].setSelected(true);
            mEmoticonTabs[i].setColorFilter(mThemeAccentColor, PorterDuff.Mode.SRC_IN);

            mEmoticonTabLastSelectedIndex = i;
        }
    }

    @Override
    public void onPageScrolled(final int i, final float v, final int i2) {
        // Don't care.
    }

    @Override
    public void onPageScrollStateChanged(final int i) {
        // Don't care.
    }

    public void showView() {
        setVisibility(VISIBLE);
    }

    public void hideView() {
        setVisibility(INVISIBLE);
    }

}
