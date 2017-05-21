package com.vanniktech.emoji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.social.tenor.model.Result;
import com.vanniktech.emoji.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class to display the GIF images from Tenor or Giphy
 */

public class TenorGridViewAdapter extends ArrayAdapter<Result> {
    private List<Result> mGridData = new ArrayList<>();

    public TenorGridViewAdapter(Context mContext, List<Result> mGridData) {
        super(mContext, R.layout.giphy_item_view, mGridData);
    }


    /**
     * Updates grid data and refresh grid items.
     * @param gridData
     */
    public void setGridData(List<Result> gridData) {
        this.mGridData.addAll(gridData);
        notifyDataSetChanged();
    }

    public void clearGridData() {
        this.mGridData.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder listViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.giphy_item_view, parent, false);
            listViewHolder = new ViewHolder();
            listViewHolder.imageInListView = (ImageView) convertView.findViewById(R.id.gifView);
            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (ViewHolder)convertView.getTag();
        }
        Result item = mGridData.get(position);

        GlideDrawableImageViewTarget drawableImgTarget =
                new GlideDrawableImageViewTarget(listViewHolder.imageInListView);
        Glide.with(listViewHolder.imageInListView.getContext()).
                load(item.getMedia().get(0).getTinygif().getUrl()).diskCacheStrategy(DiskCacheStrategy.RESULT).
                into(drawableImgTarget);

        return convertView;
    }

    @Override
    public Result getItem(int position) {
        return mGridData.get(position);
    }

    @Override
    public int getCount() {
        return mGridData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView imageInListView;
    }
}
