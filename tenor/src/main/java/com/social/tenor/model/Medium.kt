package com.social.tenor.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Medium (

    @SerializedName("nanomp4")
    @Expose
    var nanomp4: Nanomp4? = null,
    @SerializedName("nanowebm")
    @Expose
    var nanowebm: Nanowebm? = null,
    @SerializedName("tinygif")
    @Expose
    var tinygif: Tinygif? = null,
    @SerializedName("tinymp4")
    @Expose
    var tinymp4: Tinymp4? = null,
    @SerializedName("tinywebm")
    @Expose
    var tinywebm: Tinywebm? = null,
    @SerializedName("webm")
    @Expose
    var webm: Webm? = null,
    @SerializedName("gif")
    @Expose
    var gif: Gif? = null,
    @SerializedName("mp4")
    @Expose
    var mp4: Mp4? = null,
    @SerializedName("nanogif")
    @Expose
    var nanogif: Nanogif? = null,
    @SerializedName("loopedmp4")
    @Expose
    var loopedmp4: Loopedmp4? = null) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Medium> = object : Parcelable.Creator<Medium> {
            override fun createFromParcel(source: Parcel): Medium = Medium(source)
            override fun newArray(size: Int): Array<Medium?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readParcelable<Nanomp4>(Nanomp4::class.java.classLoader),
    source.readParcelable<Nanowebm>(Nanowebm::class.java.classLoader),
    source.readParcelable<Tinygif>(Tinygif::class.java.classLoader),
    source.readParcelable<Tinymp4>(Tinymp4::class.java.classLoader),
    source.readParcelable<Tinywebm>(Tinywebm::class.java.classLoader),
    source.readParcelable<Webm>(Webm::class.java.classLoader),
    source.readParcelable<Gif>(Gif::class.java.classLoader),
    source.readParcelable<Mp4>(Mp4::class.java.classLoader),
    source.readParcelable<Nanogif>(Nanogif::class.java.classLoader),
    source.readParcelable<Loopedmp4>(Loopedmp4::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(nanomp4, 0)
        dest.writeParcelable(nanowebm, 0)
        dest.writeParcelable(tinygif, 0)
        dest.writeParcelable(tinymp4, 0)
        dest.writeParcelable(tinywebm, 0)
        dest.writeParcelable(webm, 0)
        dest.writeParcelable(gif, 0)
        dest.writeParcelable(mp4, 0)
        dest.writeParcelable(nanogif, 0)
        dest.writeParcelable(loopedmp4, 0)
    }
}