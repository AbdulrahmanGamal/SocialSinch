package com.vanniktech.emoji.listeners;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Handles the click on every tab
 */

public class EmoticonTabsClickListener implements View.OnClickListener {
    private final ViewPager emojisPager;
    private final int position;

    public EmoticonTabsClickListener(final ViewPager emojisPager, final int position) {
        this.emojisPager = emojisPager;
        this.position = position;
    }

    @Override
    public void onClick(final View v) {
        emojisPager.setCurrentItem(position);
    }
}
