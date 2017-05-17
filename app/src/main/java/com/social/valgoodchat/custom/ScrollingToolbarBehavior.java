package com.social.valgoodchat.custom;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.GridView;

/**
 * Created by jorgevalbuena on 5/17/17.
 */

class ScrollingToolbarBehavior extends CoordinatorLayout.Behavior<Toolbar> {

    public ScrollingToolbarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, Toolbar child, View dependency) {
        return dependency instanceof GridView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, Toolbar child, View dependency) {
        if (dependency instanceof GridView) {

            int distanceToScroll = child.getHeight();
            int result = 0;
            TypedValue tv = new TypedValue();
            if (child.getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                result = TypedValue.complexToDimensionPixelSize(tv.data, child.getContext().getResources().getDisplayMetrics());
            }
            int bottomToolbarHeight = result;

            float ratio = dependency.getY() / (float) bottomToolbarHeight;
            CoordinatorLayout.LayoutParams lpToolbar =
                    (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            Log.e("Scrooling", "Margin bottom: " + -distanceToScroll * ratio);
            lpToolbar.bottomMargin = (int) (-distanceToScroll * ratio);
            child.setLayoutParams(lpToolbar);
        }
        return true;
    }
}
