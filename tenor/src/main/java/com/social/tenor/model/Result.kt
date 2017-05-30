package com.social.tenor.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Result (

    @SerializedName("created")
    @Expose
    var created: Double = 0.toDouble(),
    @SerializedName("url")
    @Expose
    var url: String? = null,
    @SerializedName("media")
    @Expose
    var media: List<Medium>? = null,
    @SerializedName("tags")
    @Expose
    var tags: List<Any>? = null,
    @SerializedName("shares")
    @Expose
    var shares: Long = 0,
    @SerializedName("itemurl")
    @Expose
    var itemurl: String? = null,
    @SerializedName("composite")
    @Expose
    var composite: Any? = null,
    @SerializedName("hasaudio")
    @Expose
    var isHasaudio: Boolean = false,
    @SerializedName("title")
    @Expose
    var title: String? = null,
    @SerializedName("id")
    @Expose
    var id: String? = null) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Result> = object : Parcelable.Creator<Result> {
            override fun createFromParcel(source: Parcel): Result = Result(source)
            override fun newArray(size: Int): Array<Result?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readDouble(),
    source.readString(),
    source.createTypedArrayList(Medium.CREATOR),
    ArrayList<Any>().apply { source.readList(this, Any::class.java.classLoader) },
    source.readLong(),
    source.readString(),
    source.readValue(Any::class.java.classLoader),
    1 == source.readInt(),
    source.readString(),
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeDouble(created)
        dest.writeString(url)
        dest.writeTypedList(media)
        dest.writeList(tags)
        dest.writeLong(shares)
        dest.writeString(itemurl)
        dest.writeValue(composite)
        dest.writeInt((if (isHasaudio) 1 else 0))
        dest.writeString(title)
        dest.writeString(id)
    }
}