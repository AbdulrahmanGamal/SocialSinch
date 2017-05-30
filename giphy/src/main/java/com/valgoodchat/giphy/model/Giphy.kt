package com.valgoodchat.giphy.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Giphy (
    @SerializedName("images")
    @Expose
    val images: Images? = null) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Giphy> = object : Parcelable.Creator<Giphy> {
            override fun createFromParcel(source: Parcel): Giphy = Giphy(source)
            override fun newArray(size: Int): Array<Giphy?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readParcelable<Images>(Images::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(images, 0)
    }
}