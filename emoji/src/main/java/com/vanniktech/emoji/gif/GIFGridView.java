package com.vanniktech.emoji.gif;

import android.content.Context;
import android.widget.GridLayout;
import android.widget.GridView;

import com.social.tenor.data.DataManager;
import com.vanniktech.emoji.R;
import com.vanniktech.emoji.adapter.TenorGridViewAdapter;

import java.util.ArrayList;
import java.util.Locale;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Custom GridView to obtain the vertical scroll offset
 */

public class GIFGridView extends GridView {

    private TenorGridViewAdapter mGifAdapter;
    private CompositeSubscription mDisposable;
    private String mKeywordSearch;
    private String mGifNext;
    private com.vanniktech.emoji.listeners.OnScrollListener onScrollListener;


    private final PublishSubject<String> mPaginator = PublishSubject.create();
    private static final int MAX_RESULTS = 10;

    public GIFGridView(Context context, com.vanniktech.emoji.listeners.OnScrollListener onScrollListener) {
        super(context);
        this.onScrollListener = onScrollListener;
        init();
    }

    private void init() {
        hideView();
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

        mGifAdapter = new TenorGridViewAdapter(getContext(), new ArrayList<>());
        setAdapter(mGifAdapter);
        setOnScrollListener(new EndlessScrollListener() {
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
                onScrollListener.onScrollingUp();
            }

            @Override
            public void onScrollDown() {
                onScrollListener.onScrollingDown();
            }
        });

    }

    public void showView() {
        setVisibility(VISIBLE);
    }

    public void hideView() {
        setVisibility(INVISIBLE);
    }

    @Override
    public int computeVerticalScrollOffset() {
        return super.computeVerticalScrollOffset();
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
                            return DataManager.searchGiphyByKeyword(mKeywordSearch, MAX_RESULTS, next, Locale.getDefault().toString());
                        })
                        .filter(tenorModel -> tenorModel.getResults() != null)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(tenorModel -> {
                            mGifAdapter.setGridData(tenorModel.getResults());
                            mGifNext = tenorModel.getNext();
                            onScrollListener.onLoadFinished();
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
