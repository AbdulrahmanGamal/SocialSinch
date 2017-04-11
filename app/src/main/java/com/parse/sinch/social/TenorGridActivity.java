package com.parse.sinch.social;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.parse.sinch.social.adapter.TenorGridViewAdapter;
import com.parse.sinch.social.custom.EndlessScrollListener;
import com.social.tenor.data.DataManager;
import com.social.tenor.model.Result;
import com.social.tenor.model.TenorModel;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class TenorGridActivity extends AppCompatActivity {
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private List<Result> mData;
    private String mNext;
    private TenorGridViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giphy_grid);

        mGridView = (GridView) findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mData = new ArrayList<>();
        mNext = "";
        mAdapter = new TenorGridViewAdapter(this, mData);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                loadNextDataFromApi(20);
                // or loadNextDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
        loadNextDataFromApi(20);
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`
        DataManager.getTrending(offset, mNext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TenorModel>() {
                    @Override
                    public void call(TenorModel tenorModel) {
                        mAdapter.setGridData(tenorModel.getResults());
                        mNext = tenorModel.getNext();
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }
}
