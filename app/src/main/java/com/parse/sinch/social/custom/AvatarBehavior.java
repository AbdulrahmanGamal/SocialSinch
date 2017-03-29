package com.parse.sinch.social.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.parse.sinch.social.R;

/**
 * Class to add custom behaviour to Coordinator Layout during the expansion and collapsing
 * of the Toolbar
 */
@SuppressLint("unused")
public class AvatarBehavior extends CoordinatorLayout.Behavior<SimpleDraweeView> {
    private final static float MIN_AVATAR_PERCENTAGE_SIZE   = 0.3f;
    private final static int EXTRA_FINAL_AVATAR_PADDING     = 80;

    private final static String TAG = "ImageBehavior";
    private final Context mContext;
    private int mStartXPosition;
    private float mStartToolbarPosition;
    private int mFinalMarginLeft;

    public AvatarBehavior(Context context, AttributeSet attrs) {
        this.mContext = context;
        this.mFinalMarginLeft =
                (int) context.getResources().getDimension(R.dimen.header_view_info_margin_left);
    }

    private int mStartYPosition;
    private int mFinalYPosition;
    private int finalHeight;
    private int mStartHeight;
    private int mFinalXPosition;

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, SimpleDraweeView child, View dependency) {
        return dependency instanceof Toolbar;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, SimpleDraweeView child, View dependency) {
        maybeInitProperties(child, dependency);

        final int maxScrollDistance = (int) (mStartToolbarPosition - getStatusBarHeight());
        float expandedPercentageFactor = dependency.getY() / maxScrollDistance;

        float distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
                * (1f - expandedPercentageFactor)) + (child.getHeight()/2);

        float distanceXToSubtract = ((mStartXPosition - mFinalXPosition)
                * (1f - expandedPercentageFactor)) + (child.getWidth()/2);

        float heightToSubtract = ((mStartHeight - finalHeight) * (1f - expandedPercentageFactor));

        child.setY(mStartYPosition - distanceYToSubtract);
        child.setX(mStartXPosition - distanceXToSubtract);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.width = (int) (mStartHeight - heightToSubtract);
        lp.height = (int) (mStartHeight - heightToSubtract);
        child.setLayoutParams(lp);
        return true;
    }

    @SuppressLint("PrivateResource")
    private void maybeInitProperties(SimpleDraweeView child, View dependency) {
        if (mStartYPosition == 0)
            mStartYPosition = (int) (dependency.getY());

        if (mFinalYPosition == 0)
            mFinalYPosition = (dependency.getHeight() /2);

        if (mStartHeight == 0)
            mStartHeight = child.getHeight();

        if (finalHeight == 0)
            finalHeight =
                    mContext.getResources().getDimensionPixelOffset(R.dimen.avatar_small_width);

        if (mStartXPosition == 0)
            mStartXPosition = (int) (child.getX() + (child.getWidth() / 2));

        if (mFinalXPosition == 0)
            mFinalXPosition =
                    mContext.getResources().
                            getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material) + (finalHeight / 2) + mFinalMarginLeft;

        if (mStartToolbarPosition == 0)
            mStartToolbarPosition = dependency.getY() + (dependency.getHeight()/2);
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId =
                mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
