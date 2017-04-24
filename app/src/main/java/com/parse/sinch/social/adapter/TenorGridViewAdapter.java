package com.parse.sinch.social.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.parse.sinch.social.R;
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
        //this.mGridData = mGridData;
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
            listViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.giphy_item_view, parent, false);
            listViewHolder.imageInListView = (ImageView) convertView.findViewById(R.id.gifView);
            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (ViewHolder)convertView.getTag();
        }
//        //Controller is required for controller the GIF animation, here I have just set it to autoplay as per the fresco guide.
        Result item = mGridData.get(position);
//        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setUri(item.getMedia().get(0).getTinygif().getUrl())
//                .setAutoPlayAnimations(true)
//                .build();
//        //Set the DraweeView controller, and you should be good to go.
//        draweeView.setController(controller);
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

    static class ViewHolder{
        ImageView imageInListView;
    }
}
