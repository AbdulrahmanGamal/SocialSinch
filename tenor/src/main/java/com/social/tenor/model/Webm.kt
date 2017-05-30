package com.social.tenor.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.ArrayList;

data class Webm (

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
        @JvmField @Suppress("unused") val CREATOR: Parcelable.Creator<Webm> = object : Parcelable.Creator<Webm> {
            override fun createFromParcel(source: Parcel): Webm = Webm(source)
            override fun newArray(size: Int): Array<Webm?> = arrayOfNulls(size)
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
