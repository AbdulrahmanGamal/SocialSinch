package com.social.valgoodchat.custom;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.social.tenor.data.DataManager;
import com.social.valgoodchat.R;
import com.social.valgoodchat.adapter.TenorGridViewAdapter;

import java.util.ArrayList;
import java.util.Locale;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by jorgevalbuena on 5/17/17.
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
        init(LayoutInflater.from(getContext()).inflate(R.layout.gif_layout_view, this, true));
    }

    private void init(View v) {
        setBackgroundColor(ContextCompat.getColor(getContext(), com.vanniktech.emoji.R.color.emoji_background));
        GridView gridView = (GridView) findViewById(R.id.gridView);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBottomMenu);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mLocale = Locale.getDefault().toString();
        mGifAdapter = new TenorGridViewAdapter(v.getContext(), new ArrayList<>());
        gridView.setAdapter(mGifAdapter);
        gridView.setOnScrollListener(new EndlessScrollListener(getContext()) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                if (!"".equals(mGifNext)) {
                    mPaginator.onNext(mGifNext);
                    return true;
                }

                return false;
            }

            @Override
            public void onMoved(int distance) {
                Log.e(TAG, "Setting translation to: " + distance);
                toolbar.setTranslationY(-distance);
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
        Log.e(TAG, "View Detached from Window");
        mDisposable.unsubscribe();
        super.onDetachedFromWindow();
    }

    @Override
    public void onAttachedToWindow() {
        Log.e(TAG, "View Attached to Window");
        mDisposable = new CompositeSubscription();
        attachPaginator();
        mPaginator.onNext(mGifNext);
        super.onAttachedToWindow();
    }
}
