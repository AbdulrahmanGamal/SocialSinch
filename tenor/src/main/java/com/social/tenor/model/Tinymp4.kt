package com.social.tenor.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Tinymp4 (
    @SerializedName("url")
    @Expose
    var url: String? = null,
    @SerializedName("dims")
    @Expose
    var dims: List<Long>? = null,
    @SerializedName("duration")
    @Expose
    var duration: Double = 0.toDouble(),
    @SerializedName("preview")
    @Expose
    var preview: String? = null) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Tinymp4> = object : Parcelable.Creator<Tinymp4> {
            override fun createFromParcel(source: Parcel): Tinymp4 = Tinymp4(source)
            override fun newArray(size: Int): Array<Tinymp4?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readString(),
    ArrayList<Long>().apply { source.readList(this, Long::class.java.classLoader) },
    source.readDouble(),
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(url)
        dest.writeList(dims)
        dest.writeDouble(duration)
        dest.writeString(preview)
    }
}