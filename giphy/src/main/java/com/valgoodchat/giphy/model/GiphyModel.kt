package com.valgoodchat.giphy.model

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GiphyModel (
    @SerializedName("data")
    @Expose
    var data: List<Giphy> = ArrayList(),
    @SerializedName("pagination")
    @Expose
    var pagination: Pagination? = null,
    @SerializedName("meta")
    @Expose
    var meta: Meta? = null) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<GiphyModel> = object : Parcelable.Creator<GiphyModel> {
            override fun createFromParcel(source: Parcel): GiphyModel = GiphyModel(source)
            override fun newArray(size: Int): Array<GiphyModel?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.createTypedArrayList(Giphy.CREATOR),
    source.readParcelable<Pagination>(Pagination::class.java.classLoader),
    source.readParcelable<Meta>(Meta::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(data)
        dest.writeParcelable(pagination, 0)
        dest.writeParcelable(meta, 0)
    }
}