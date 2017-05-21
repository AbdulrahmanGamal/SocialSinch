package com.vanniktech.emoji.gif;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;
import android.widget.GridView;

import com.vanniktech.emoji.R;

/**
 * Custom GridView to obtain the vertical scroll offset
 */

public class GIFGridView extends GridView {

    public GIFGridView(Context context) {
        super(context);
        init();
    }

    public GIFGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GIFGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int spacingMargins = getContext().getResources().
                getDimensionPixelSize(R.dimen.gif_spacing_and_margins);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.setMargins(spacingMargins,spacingMargins,spacingMargins,spacingMargins);
        layoutParams.setGravity(TEXT_ALIGNMENT_CENTER);
        setLayoutParams(layoutParams);
        setColumnWidth(getContext().getResources().getDimensionPixelSize(R.dimen.gif_column_width));
        setDrawSelectorOnTop(true);
        setNumColumns(AUTO_FIT);
        setStretchMode(STRETCH_COLUMN_WIDTH);
        setVerticalSpacing(spacingMargins);
        setHorizontalSpacing(spacingMargins);
        setFocusable(true);
        setTranscriptMode(TRANSCRIPT_MODE_DISABLED);
        setClickable(true);

    }

    @Override
    public int computeVerticalScrollOffset() {
        return super.computeVerticalScrollOffset();
    }
}
