package com.valgoodchat.giphy.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Images (

    @SerializedName("fixed_height_downsampled")
    @Expose
    val fixedHeightDownsampled: FixedHeightDownsampled? = null) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Images> = object : Parcelable.Creator<Images> {
            override fun createFromParcel(source: Parcel): Images = Images(source)
            override fun newArray(size: Int): Array<Images?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readParcelable<FixedHeightDownsampled>(FixedHeightDownsampled::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(fixedHeightDownsampled, 0)
    }
}
