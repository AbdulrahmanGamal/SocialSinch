package com.vanniktech.emoji.gif;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.social.tenor.data.DataManager;
import com.vanniktech.emoji.R;
import com.vanniktech.emoji.Utils;
import com.vanniktech.emoji.adapter.TenorGridViewAdapter;

import java.util.ArrayList;
import java.util.Locale;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Gif View that is shown inside the bottom pop up
 */

public class GIFView extends FrameLayout {

    private String mGifNext;
    private ProgressBar mProgressBar;
    private TenorGridViewAdapter mGifAdapter;
    private CompositeSubscription mDisposable;
    private String mKeywordSearch;
    private String mLocale;

    private final PublishSubject<String> mPaginator = PublishSubject.create();

    private static final int MAX_RESULTS = 10;
    private static final String TAG = "GIFView";

    public GIFView(Context context) {
        super(context);
        init();
    }

    public GIFView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GIFView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        init(LayoutInflater.from(getContext()).inflate(R.layout.general_layout_view, this, true));
    }

    private void init(View v) {
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.emoji_background));
        GridView gridView = new GIFGridView(getContext());
        addView(gridView, 0);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBottomMenu);
        final int mToolbarHeight = Utils.getToolbarHeight(toolbar.getContext());
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mLocale = Locale.getDefault().toString();
        mGifAdapter = new TenorGridViewAdapter(v.getContext(), new ArrayList<>());
        gridView.setAdapter(mGifAdapter);
        gridView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                if (!"".equals(mGifNext)) {
                    mPaginator.onNext(mGifNext);
                    return true;
                }

                return false;
            }

            @Override
            public void onScrollUp() {
                toolbar.animate().translationY(0).
                        setInterpolator(new AccelerateInterpolator()).start();
            }

            @Override
            public void onScrollDown() {
                toolbar.animate().translationY(mToolbarHeight).
                        setInterpolator(new AccelerateInterpolator()).start();
            }
        });
    }

    /**
     * Subscribes to the paginator observable
     */
    private void attachPaginator() {
        mGifNext = "";
        mKeywordSearch = "";
        mDisposable.add(
                mPaginator
                        .observeOn(Schedulers.io())
                        .concatMap(next -> { if ("".equals(mKeywordSearch)) {
                            return DataManager.getTrending(MAX_RESULTS, next);
                        }
                            return DataManager.searchGiphyByKeyword(mKeywordSearch, MAX_RESULTS, next, mLocale);
                        })
                        .filter(tenorModel -> tenorModel.getResults() != null)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(tenorModel -> {
                            mGifAdapter.setGridData(tenorModel.getResults());
                            mGifNext = tenorModel.getNext();
                            mProgressBar.setVisibility(View.INVISIBLE);
                        })
        );
    }

    @Override
    public void onDetachedFromWindow() {
        mDisposable.unsubscribe();
        super.onDetachedFromWindow();
    }

    @Override
    public void onAttachedToWindow() {
        mDisposable = new CompositeSubscription();
        attachPaginator();
        mPaginator.onNext(mGifNext);
        super.onAttachedToWindow();
    }
}
