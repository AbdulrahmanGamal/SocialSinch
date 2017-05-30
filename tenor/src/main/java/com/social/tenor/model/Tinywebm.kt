package com.social.tenor.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Tinywebm (
    @SerializedName("url")
    @Expose
    var url: String? = null,
    @SerializedName("dims")
    @Expose
    var dims: List<Long>? = null,
    @SerializedName("preview")
    @Expose
    var preview: String? = null) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Tinywebm> = object : Parcelable.Creator<Tinywebm> {
            override fun createFromParcel(source: Parcel): Tinywebm = Tinywebm(source)
            override fun newArray(size: Int): Array<Tinywebm?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readString(),
    ArrayList<Long>().apply { source.readList(this, Long::class.java.classLoader) },
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(url)
        dest.writeList(dims)
        dest.writeString(preview)
    }
}

