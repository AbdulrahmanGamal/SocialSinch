package com.vanniktech.emoji.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import com.vanniktech.emoji.R
import com.vanniktech.emoji.data.EmoticonDataManager

/**
 * Adapter class to display the GIF images from Tenor or Giphy
 */

class AnimatedGridViewAdapter(context: Context,
                              val data: MutableList<String>,
                              val isRecent: Boolean) :
        ArrayAdapter<String>(context, R.layout.giphy_item_view, data) {

    /**
     * Updates grid data and refresh grid items.
     * @param gridData
     */
    fun setGridData(gridData: List<String>) {
        data.addAll(gridData)
        notifyDataSetChanged()
    }

    fun clearGridData() {
        data.clear()
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row : View? = convertView
        val listViewHolder: ViewHolder

        if (row == null) {
            row = LayoutInflater.from(parent.context).inflate(R.layout.giphy_item_view, parent, false)
            listViewHolder = ViewHolder()
            listViewHolder.imageInListView = row?.findViewById(R.id.gifView) as ImageView
            row.tag = listViewHolder
        } else {
            listViewHolder = row.tag as ViewHolder
        }
        val itemURL = data[position]

        val drawableImgTarget = GlideDrawableImageViewTarget(listViewHolder.imageInListView!!)
        Glide.with(listViewHolder.imageInListView!!.context).load(itemURL).diskCacheStrategy(DiskCacheStrategy.RESULT).into(drawableImgTarget)

        row.setOnClickListener { _ ->
            if (!isRecent) {
                EmoticonDataManager.getInstance(context).addRecent(itemURL.trim { it <= ' ' })
            }
        }
        return row
    }

    override fun getItem(position: Int): String = data[position]
    override fun getCount(): Int = data.size
    override fun getItemId(position: Int): Long = position.toLong()

    internal class ViewHolder {
        var imageInListView: ImageView? = null
    }
}
