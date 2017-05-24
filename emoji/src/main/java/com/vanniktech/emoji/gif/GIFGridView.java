package com.vanniktech.emoji.gif;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.GridLayout;

import com.social.tenor.data.DataManager;
import com.social.tenor.model.Result;
import com.vanniktech.emoji.R;
import com.vanniktech.emoji.adapter.AnimatedGridViewAdapter;
import com.vanniktech.emoji.custom.BasicGridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Custom GridView to obtain the vertical scroll offset
 */

public class GIFGridView extends BasicGridView {

    private AnimatedGridViewAdapter mGifAdapter;
    private CompositeSubscription mDisposable;
    private String mKeywordSearch;
    private String mGifNext;

    private final PublishSubject<String> mPaginator = PublishSubject.create();
    private static final int MAX_RESULTS = 10;

    public GIFGridView(Context context, @NonNull Toolbar bottomToolbar) {
        super(context, bottomToolbar);
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
        setStretchMode(STRETCH_COLUMN_WIDTH);
        setVerticalSpacing(spacingMargins);
        setHorizontalSpacing(spacingMargins);

        mGifAdapter = new AnimatedGridViewAdapter(getContext(), new ArrayList<>(), false);
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
                            return DataManager.searchGiphyByKeyword(mKeywordSearch, MAX_RESULTS, next, Locale.getDefault().toString());
                        })
                        .filter(tenorModel -> tenorModel.getResults() != null)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(tenorModel -> {
                            final List<String> results = new ArrayList<>();
                            for (Result item: tenorModel.getResults()) {
                                results.add(item.getMedia().get(0).getTinygif().getUrl());
                            }
                            mGifAdapter.setGridData(results);
                            mGifNext = tenorModel.getNext();
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
