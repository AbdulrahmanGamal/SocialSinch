package com.vanniktech.emoji.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.vanniktech.emoji.custom.RecentEmoticonGridView;
import com.vanniktech.emoji.gif.GIFGridView;
import com.vanniktech.emoji.gif.GiphyGridView;

/**
 * Adapter to load the emoticons in each page
 */

public class EmoticonPagerAdapter extends PagerAdapter {
    private Toolbar mBottomToolbar;
    private RecentEmoticonGridView mRecentEmoticonGridView;

    public EmoticonPagerAdapter(Toolbar bottomToolbar) {
        this.mBottomToolbar = bottomToolbar;
    }

    @Override
    public Object instantiateItem(final ViewGroup pager, final int position) {
        final View newView;

        switch (position) {
            case 0:
                newView = new RecentEmoticonGridView(pager.getContext(), mBottomToolbar);
                mRecentEmoticonGridView = ((RecentEmoticonGridView)newView);
                break;
            case 1:
                newView = new GIFGridView(pager.getContext(), mBottomToolbar);
                break;
            case 2:
            default:
                newView = new GiphyGridView(pager.getContext(), mBottomToolbar);
                break;
        }

        pager.addView(newView);

        return newView;
    }

    @Override
    public void destroyItem(final ViewGroup pager, final int position, final Object view) {
        pager.removeView((View) view);
        if (position == 0) {
            mRecentEmoticonGridView = null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    public void invalidateRecent() {
        if (mRecentEmoticonGridView != null) {
            mRecentEmoticonGridView.refreshEmoticons();
        }
    }
}
