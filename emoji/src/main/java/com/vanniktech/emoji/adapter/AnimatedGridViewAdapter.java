package com.vanniktech.emoji.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.vanniktech.emoji.R;
import com.vanniktech.emoji.data.EmoticonDataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class to display the GIF images from Tenor or Giphy
 */

public class AnimatedGridViewAdapter extends ArrayAdapter<String> {
    private List<String> mGridData = new ArrayList<>();
    private boolean isRecent;

    public AnimatedGridViewAdapter(Context mContext, List<String> gridData, boolean isRecent) {
        super(mContext, R.layout.giphy_item_view, gridData);
        this.mGridData = gridData;
        this.isRecent = isRecent;
    }


    /**
     * Updates grid data and refresh grid items.
     * @param gridData
     */
    public void setGridData(List<String> gridData) {
        this.mGridData.addAll(gridData);
        notifyDataSetChanged();
    }

    public void clearGridData() {
        this.mGridData.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

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
        String itemURL = mGridData.get(position);

        GlideDrawableImageViewTarget drawableImgTarget =
                new GlideDrawableImageViewTarget(listViewHolder.imageInListView);
        Glide.with(listViewHolder.imageInListView.getContext()).
                load(itemURL).diskCacheStrategy(DiskCacheStrategy.RESULT).
                into(drawableImgTarget);

        convertView.setOnClickListener(v -> {
            if (!isRecent) {
                EmoticonDataManager.getInstance(getContext()).addRecent(itemURL.trim());
            }
        });
        return convertView;
    }

    @Override
    public String getItem(int position) {
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
