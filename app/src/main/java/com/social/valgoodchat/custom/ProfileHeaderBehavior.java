package com.social.valgoodchat.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.social.valgoodchat.R;


/**
 * Class to add custom behaviour to Coordinator Layout during the expansion and collapsing
 * of the Toolbar
 */
@SuppressLint("unused")
public class ProfileHeaderBehavior extends CoordinatorLayout.Behavior<ProfileHeaderView> {
    private static final float MAX_SCALE = 0.5f;

    private Context mContext;

    private int mStartMarginLeftTitle;
    private int mStartMarginLeftSubTitle;
    private int mEndMargintLeft;
    private int mMarginRight;
    private int mStartMarginBottom;
    private float mStartToolbarPosition;
    private int mStartYPosition;
    private int mFinalYPosition;
    private int mFinalMarginTop;

    public ProfileHeaderBehavior(Context context, AttributeSet attrs) {
        mContext = context;
        this.mFinalMarginTop =
                (int) context.getResources().getDimension(R.dimen.header_view_start_margin_top);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ProfileHeaderView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ProfileHeaderView child, View dependency) {
        shouldInitProperties(child, dependency);

        int maxScroll = ((AppBarLayout) dependency).getTotalScrollRange();
        float percentage = Math.abs(dependency.getY()) / (float) maxScroll;

        // Set scale for the title
        float size = ((1 - percentage) * MAX_SCALE) + 1;
        child.setScaleXTitle(size);
        child.setScaleYTitle(size);

        // Set position for the header view
        float childPosition = dependency.getHeight()/1.4f
                + dependency.getY();

        if (childPosition > mFinalMarginTop) {
            child.setY(childPosition);
        }
        // Set Margin for title
        RelativeLayout.LayoutParams lpTitle =
                (RelativeLayout.LayoutParams) child.getName().getLayoutParams();
        lpTitle.leftMargin =
                (int) ((mStartMarginLeftTitle) - (percentage * (mStartMarginLeftTitle - mEndMargintLeft)));

        if (lpTitle.leftMargin < 20) {
            lpTitle.leftMargin = 20;
        }
        lpTitle.rightMargin = mMarginRight;
        child.getName().setLayoutParams(lpTitle);

        // Set Margin for subtitle
        RelativeLayout.LayoutParams lpSubTitle =
                (RelativeLayout.LayoutParams) child.getLastSeen().getLayoutParams();
        lpSubTitle.leftMargin =
                (int) ((mStartMarginLeftSubTitle) - (percentage * (mStartMarginLeftSubTitle - mEndMargintLeft)));

        if (lpSubTitle.leftMargin < 20) {
            lpSubTitle.leftMargin = 20;
        }
        lpSubTitle.rightMargin = mMarginRight;
        child.getLastSeen().setLayoutParams(lpSubTitle);

        return true;
    }

    private void shouldInitProperties(ProfileHeaderView child, View dependency) {

        if (mStartMarginLeftTitle == 0)
            mStartMarginLeftTitle = getStartMarginLeftTitle(child);

        if (mStartMarginLeftSubTitle == 0)
            mStartMarginLeftSubTitle = getStartMarginLeftSubTitle(child);

        if (mEndMargintLeft == 0)
            mEndMargintLeft =
                    mContext.getResources().getDimensionPixelOffset(R.dimen.header_view_end_margin_left);

        if (mStartMarginBottom == 0)
            mStartMarginBottom =
                    mContext.getResources().getDimensionPixelOffset(R.dimen.header_view_start_margin_bottom);

        if (mMarginRight == 0)
            mMarginRight =
                    mContext.getResources().getDimensionPixelOffset(R.dimen.header_view_end_margin_right);

        if (mStartToolbarPosition == 0)
            mStartToolbarPosition = dependency.getY() + (dependency.getHeight()/2);

        if (mStartYPosition == 0)
            mStartYPosition = (int) (dependency.getY());

        if (mFinalYPosition == 0)
            mFinalYPosition = (dependency.getHeight() /2);
    }

    public int getStartMarginLeftTitle(ProfileHeaderView headerView) {
        TextView title = headerView.getName();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;

        int stringWidth = getStringWidth(title);

        //margin left calculation
        return (int) ((width / 2) - ((stringWidth + (stringWidth * MAX_SCALE)) / 2));

    }

    public int getStartMarginLeftSubTitle(ProfileHeaderView headerView) {
        TextView subTitle = headerView.getLastSeen();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;

        int stringWidth = getStringWidth(subTitle);

        //margin left calculation
        return ((width / 2) - (stringWidth / 2));
    }

    public int getStringWidth(TextView textView) {
        Rect bounds = new Rect();
        Paint textPaint = textView.getPaint();
        textPaint.getTextBounds(textView.getText().toString(), 0, textView.getText().toString().length(), bounds);
        return bounds.width();
    }

//    public int getToolbarHeight() {
//        int result = 0;
//        TypedValue tv = new TypedValue();
//        if (mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
//            result = TypedValue.complexToDimensionPixelSize(tv.data, mContext.getResources().getDisplayMetrics());
//        }
//        return result;
//    }
//    private int getToolbarHeight() {
//    int result = 0;
//    int resourceId =
//            mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
//
//    if (resourceId > 0) {
//        result = mContext.getResources().getDimensionPixelSize(resourceId);
//    }
//    return result;
//}
}
