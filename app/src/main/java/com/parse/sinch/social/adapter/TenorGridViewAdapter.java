package com.parse.sinch.social.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.parse.sinch.social.R;
import com.parse.sinch.social.custom.GiphyWebView;
import com.social.tenor.model.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by valgood on 4/9/2017.
 */

public class TenorGridViewAdapter extends ArrayAdapter<Result> {
    private List<Result> mGridData = new ArrayList<>();

    public TenorGridViewAdapter(Context mContext, List<Result> mGridData) {
        super(mContext, R.layout.giphy_item_view, mGridData);
        this.mGridData = mGridData;
    }


    /**
     * Updates grid data and refresh grid items.
     * @param gridData
     */
    public void setGridData(List<Result> gridData) {
        if (mGridData == null) {
            this.mGridData = gridData;
        } else {
            this.mGridData.addAll(gridData);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;

        if (convertView == null) {
            row = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.giphy_item_view, parent, false);
        } else {
            row = convertView;
        }
        Result item = mGridData.get(position);
        ((GiphyWebView)row).bindTo(item.getMedia().get(0).getGif().getUrl());
        return row;
    }

    @Override
    public int getCount() {
        return mGridData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
