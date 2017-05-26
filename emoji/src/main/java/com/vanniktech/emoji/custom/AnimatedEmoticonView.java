package com.vanniktech.emoji.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.vanniktech.emoji.R;
import com.vanniktech.emoji.adapter.EmoticonPagerAdapter;
import com.vanniktech.emoji.data.EmoticonDataManager;

/**
 * Specific implementation for Animated emoticons
 */
@SuppressLint("ViewConstructor")
public class AnimatedEmoticonView extends BasicEmoticonView {
    private final EmoticonPagerAdapter mEmoticonPagerAdapter;

    public AnimatedEmoticonView(Context context, Toolbar toolbarBottom) {
        super(context);

        final ViewPager emoticonPager = (ViewPager) findViewById(R.id.emojis_pager);
        final LinearLayout emoticonTab = (LinearLayout) findViewById(R.id.emojis_tab);
        emoticonPager.addOnPageChangeListener(this);

        mEmoticonTabs = new ImageButton[4];
        mEmoticonTabs[0] = inflateButton(context, R.drawable.emoji_recent, emoticonTab);
        mEmoticonTabs[1] = inflateButton(context, R.drawable.input_gif, emoticonTab);
        mEmoticonTabs[2] = inflateButton(context, R.drawable.ic_tenor, emoticonTab);
        mEmoticonTabs[3] = inflateButton(context, R.drawable.ic_meme, emoticonTab);

        handleOnClicks(emoticonPager);

        mEmoticonPagerAdapter = new EmoticonPagerAdapter(toolbarBottom);
        emoticonPager.setAdapter(mEmoticonPagerAdapter);

        final int startIndex = EmoticonDataManager.getInstance(context).getTotalRecent() > 0 ? 0 : 1;
        emoticonPager.setCurrentItem(startIndex);
        onPageSelected(startIndex);
    }

    @Override
    public void onPageSelected(int position) {
        if (mEmoticonTabLastSelectedIndex != position) {
            if (position == 0) {
                mEmoticonPagerAdapter.invalidateRecent();
            }
        }
        super.onPageSelected(position);
    }
}
