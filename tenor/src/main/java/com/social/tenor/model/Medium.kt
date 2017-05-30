package com.social.tenor.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Medium (

    @SerializedName("tinygif")
    @Expose
    var tinygif: Tinygif? = null) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Medium> = object : Parcelable.Creator<Medium> {
            override fun createFromParcel(source: Parcel): Medium = Medium(source)
            override fun newArray(size: Int): Array<Medium?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readParcelable<Tinygif>(Tinygif::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(tinygif, 0)
    }
}
