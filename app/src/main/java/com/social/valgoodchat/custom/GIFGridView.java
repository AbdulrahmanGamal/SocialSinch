package com.social.valgoodchat.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Custom GridView to obtain the vertical scroll offset
 */

public class GIFGridView extends GridView {

    public GIFGridView(Context context) {
        super(context);
    }

    public GIFGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GIFGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int computeVerticalScrollOffset() {
        return super.computeVerticalScrollOffset();
    }
}
