package com.vanniktech.emoji.meme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.GridLayout;

import com.valgoodchat.meme.data.DataManager;
import com.valgoodchat.meme.model.Meme;
import com.vanniktech.emoji.R;
import com.vanniktech.emoji.adapter.AnimatedGridViewAdapter;
import com.vanniktech.emoji.custom.BasicGridView;
import com.vanniktech.emoji.gif.EndlessScrollListener;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Custom GridView to obtain memes from ImgFlip
 */
@SuppressLint("ViewConstructor")
public class MemeGridView extends BasicGridView {

    private AnimatedGridViewAdapter mMemeAdapter;
    private CompositeSubscription mDisposable;

    public MemeGridView(Context context, @NonNull Toolbar bottomToolbar) {
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

        mMemeAdapter = new AnimatedGridViewAdapter(getContext(), new ArrayList<>(), false);
        setAdapter(mMemeAdapter);

        mDisposable = new CompositeSubscription();
        mDisposable.add( DataManager.getTrending()
                                    .subscribeOn(Schedulers.io())
                                    .filter(memeModel -> memeModel.getData() != null)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(memeModel -> {
                                        final List<String> results = new ArrayList<>();
                                        for (Meme item: memeModel.getData().getMemes()) {
                                            results.add(item.getUrl());
                                        }
                                        mMemeAdapter.setGridData(results);
                                    })
        );
        setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                //this API doesn't support pagination
                return false;
            }
        });

    }

    @Override
    public void onDetachedFromWindow() {
        mDisposable.unsubscribe();
        super.onDetachedFromWindow();
    }
}
