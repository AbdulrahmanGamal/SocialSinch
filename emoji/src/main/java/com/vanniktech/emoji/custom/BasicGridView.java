package com.vanniktech.emoji.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.animation.AccelerateInterpolator;
import android.widget.GridView;

import com.vanniktech.emoji.Constants;
import com.vanniktech.emoji.gif.EndlessScrollListener;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.vanniktech.emoji.UtilsKt.getToolbarHeight;

/**
 * Basic class to create a GridView
 */

public abstract class BasicGridView extends GridView {
    private Subscription mSubscription;

    public BasicGridView(Context context, @NonNull Toolbar bottomToolbar) {
        super(context);
        init(bottomToolbar);
    }

    private void init(final Toolbar bottomToolbar) {
        setNumColumns(AUTO_FIT);
        setClipToPadding(false);
        setVerticalScrollBarEnabled(false);
        setFocusable(true);
        setTranscriptMode(TRANSCRIPT_MODE_DISABLED);
        setClickable(true);
        final int toolbarHeight = getToolbarHeight(getContext());
        //subscribe to scrolling events
        mSubscription = EndlessScrollListener.getScrollingDirectionSubject().
                        onBackpressureDrop().
                        subscribe(scrollingType -> {
                            if (scrollingType == Constants.SCROLLING_UP) {
                                bottomToolbar.animate().translationY(0).
                                        setInterpolator(new AccelerateInterpolator()).start();
                            } else {
                                bottomToolbar.animate().translationY(toolbarHeight).
                                        setInterpolator(new AccelerateInterpolator()).start();
                            }
                        });


    }

    @Override
    public int computeVerticalScrollOffset() {
        return super.computeVerticalScrollOffset();
    }

    @Override
    public void onDetachedFromWindow() {
        mSubscription.unsubscribe();
        super.onDetachedFromWindow();
    }

    @Override
    public void onAttachedToWindow() {
        mSubscription = new CompositeSubscription();
        super.onAttachedToWindow();
    }
}
