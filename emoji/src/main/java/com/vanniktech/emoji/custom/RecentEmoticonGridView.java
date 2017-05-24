package com.vanniktech.emoji.custom;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.widget.GridLayout;

import com.vanniktech.emoji.R;
import com.vanniktech.emoji.adapter.AnimatedGridViewAdapter;
import com.vanniktech.emoji.data.EmoticonDataManager;

import java.util.List;

/**
 * GridView to show the most recent animated emoticons used
 */

public class RecentEmoticonGridView extends BasicGridView {

    public RecentEmoticonGridView(Context context, Toolbar bottomToolbar) {
        super(context, bottomToolbar);
        int spacingMargins = getContext().getResources().
                getDimensionPixelSize(R.dimen.gif_spacing_and_margins);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.setMargins(spacingMargins,spacingMargins,spacingMargins,spacingMargins);
        layoutParams.setGravity(TEXT_ALIGNMENT_CENTER);
        setLayoutParams(layoutParams);
        setColumnWidth(getContext().getResources().getDimensionPixelSize(R.dimen.gif_column_width));
        setDrawSelectorOnTop(true);
        setStretchMode(STRETCH_COLUMN_WIDTH);
        setVerticalSpacing(spacingMargins);
        setHorizontalSpacing(spacingMargins);
        refreshEmoticons();
    }

    public void refreshEmoticons() {
        List<String> recentAnimatedEmoticons = EmoticonDataManager.getInstance(getContext()).getRecent();
        setAdapter(new AnimatedGridViewAdapter(getContext(), recentAnimatedEmoticons, true));
    }


}
