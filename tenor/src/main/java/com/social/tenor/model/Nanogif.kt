package com.social.tenor.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Nanogif (
    @SerializedName("url")
    @Expose
    var url: String? = null,
    @SerializedName("dims")
    @Expose
    var dims: List<Long>? = null,
    @SerializedName("preview")
    @Expose
    var preview: String? = null,
    @SerializedName("size")
    @Expose
    var size: Long = 0) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Nanogif> = object : Parcelable.Creator<Nanogif> {
            override fun createFromParcel(source: Parcel): Nanogif = Nanogif(source)
            override fun newArray(size: Int): Array<Nanogif?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readString(),
    ArrayList<Long>().apply { source.readList(this, Long::class.java.classLoader) },
    source.readString(),
    source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(url)
        dest.writeList(dims)
        dest.writeString(preview)
        dest.writeLong(size)
    }
}