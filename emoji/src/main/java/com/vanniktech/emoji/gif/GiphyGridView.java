package com.vanniktech.emoji.gif;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.GridLayout;

import com.valgoodchat.giphy.data.DataManager;
import com.valgoodchat.giphy.model.Giphy;
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
 * Custom GridView to obtain gif frmo Giphy
 */
@SuppressLint("ViewConstructor")
public class GiphyGridView extends BasicGridView {

    private AnimatedGridViewAdapter mGiphyAdapter;
    private CompositeSubscription mDisposable;
    private String mKeywordSearch;
    private String mGiphyNext;

    private final PublishSubject<String> mPaginator = PublishSubject.create();
    private static final int MAX_RESULTS = 25;

    public GiphyGridView(Context context, @NonNull Toolbar bottomToolbar) {
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

        mGiphyAdapter = new AnimatedGridViewAdapter(getContext(), new ArrayList<>(), false);
        setAdapter(mGiphyAdapter);
        setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                if (!"".equals(mGiphyNext)) {
                    mPaginator.onNext(mGiphyNext);
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
        mGiphyNext = "";
        mKeywordSearch = "";
        mDisposable.add(
                mPaginator
                        .observeOn(Schedulers.io())
                        .concatMap(next -> { if ("".equals(mKeywordSearch)) {
                            return DataManager.INSTANCE.getTrending(MAX_RESULTS, next);
                        }
                            return DataManager.INSTANCE.searchGiphyByKeyword(mKeywordSearch, MAX_RESULTS,
                                                            next, Locale.getDefault().toString());
                        })
                        .filter(giphyModel -> giphyModel.getData() != null)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(giphyModel -> {
                            final List<String> results = new ArrayList<>();
                            for (Giphy item: giphyModel.getData()) {
                                results.add(item.getImages().getFixedHeightDownsampled().getUrl());
                            }
                            mGiphyAdapter.setGridData(results);
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
        mPaginator.onNext(mGiphyNext);
        super.onAttachedToWindow();
    }
}
