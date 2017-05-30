package com.valgoodchat.giphy.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Pagination (
    @SerializedName("total_count")
    @Expose
    var totalCount: Long = 0,
    @SerializedName("count")
    @Expose
    var count: Long = 0,
    @SerializedName("offset")
    @Expose
    var offset: Long = 0) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Pagination> = object : Parcelable.Creator<Pagination> {
            override fun createFromParcel(source: Parcel): Pagination = Pagination(source)
            override fun newArray(size: Int): Array<Pagination?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readLong(),
    source.readLong(),
    source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(totalCount)
        dest.writeLong(count)
        dest.writeLong(offset)
    }
}