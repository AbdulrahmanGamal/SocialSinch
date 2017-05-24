package com.vanniktech.emoji.custom;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.vanniktech.emoji.R;
import com.vanniktech.emoji.adapter.EmoticonPagerAdapter;
import com.vanniktech.emoji.data.EmoticonDataManager;
import com.vanniktech.emoji.listeners.EmoticonTabsClickListener;

/**
 * Created by jorgevalbuena on 5/24/17.
 */

public class CustomEmoticonView extends LinearLayout implements ViewPager.OnPageChangeListener {

    @ColorInt
    private final int mThemeAccentColor;
    @ColorInt
    private final int mThemeIconColor;
    private final ImageButton[] mEmoticonTabs;
    private final EmoticonPagerAdapter mEmoticonPagerAdapter;
    private int mEmoticonTabLastSelectedIndex = -1;


    public CustomEmoticonView(Context context, Toolbar toolbarBottom) {
        super(context);
        View.inflate(context, R.layout.emoji_view, this);
        setOrientation(VERTICAL);
        setBackgroundColor(ContextCompat.getColor(context, R.color.emoji_background));

        mThemeIconColor = ContextCompat.getColor(context, R.color.emoji_icons);
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        mThemeAccentColor = value.data;

        final ViewPager emoticonPager = (ViewPager) findViewById(R.id.emojis_pager);
        final LinearLayout emoticonTab = (LinearLayout) findViewById(R.id.emojis_tab);
        emoticonPager.addOnPageChangeListener(this);

        mEmoticonTabs = new ImageButton[2];
        mEmoticonTabs[0] = inflateButton(context, R.drawable.emoji_recent, emoticonTab);
        mEmoticonTabs[1] = inflateButton(context, R.drawable.input_gif, emoticonTab);
        //mEmoticonTabs[2] = inflateButton(context, R.drawable.input_gif, emoticonTab);

        handleOnClicks(emoticonPager);

        mEmoticonPagerAdapter = new EmoticonPagerAdapter(toolbarBottom);
        emoticonPager.setAdapter(mEmoticonPagerAdapter);

        final int startIndex = EmoticonDataManager.getInstance(context).getTotalRecent() > 0 ? 0 : 1;
        emoticonPager.setCurrentItem(startIndex);
        onPageSelected(startIndex);
    }

    public void showView() {
        setVisibility(VISIBLE);
    }

    public void hideView() {
        setVisibility(INVISIBLE);
    }

    private void handleOnClicks(final ViewPager emojisPager) {
        for (int i = 0; i < mEmoticonTabs.length; i++) {
            mEmoticonTabs[i].setOnClickListener(new EmoticonTabsClickListener(emojisPager, i));
        }
    }

    private ImageButton inflateButton(final Context context, @DrawableRes final int icon, final ViewGroup parent) {
        final ImageButton button = (ImageButton) LayoutInflater.from(context).inflate(R.layout.emoji_category, parent, false);

        button.setImageDrawable(AppCompatResources.getDrawable(context, icon));
        button.setColorFilter(mThemeIconColor, PorterDuff.Mode.SRC_IN);

        parent.addView(button);

        return button;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mEmoticonTabLastSelectedIndex != position) {
            if (position == 0) {
                mEmoticonPagerAdapter.invalidateRecent();
            }

            if (mEmoticonTabLastSelectedIndex >= 0 && mEmoticonTabLastSelectedIndex < mEmoticonTabs.length) {
                mEmoticonTabs[mEmoticonTabLastSelectedIndex].setSelected(false);
                mEmoticonTabs[mEmoticonTabLastSelectedIndex].setColorFilter(mThemeIconColor, PorterDuff.Mode.SRC_IN);
            }

            mEmoticonTabs[position].setSelected(true);
            mEmoticonTabs[position].setColorFilter(mThemeAccentColor, PorterDuff.Mode.SRC_IN);

            mEmoticonTabLastSelectedIndex = position;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
