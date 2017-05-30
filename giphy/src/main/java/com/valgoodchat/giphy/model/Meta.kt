package com.valgoodchat.giphy.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Meta (
    @SerializedName("status")
    @Expose
    var status: Long = 0,
    @SerializedName("msg")
    @Expose
    var msg: String? = null,
    @SerializedName("response_id")
    @Expose
    var responseId: String? = null) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Meta> = object : Parcelable.Creator<Meta> {
            override fun createFromParcel(source: Parcel): Meta = Meta(source)
            override fun newArray(size: Int): Array<Meta?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readLong(),
    source.readString(),
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(status)
        dest.writeString(msg)
        dest.writeString(responseId)
    }
}