package com.social.tenor.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Result (

    @SerializedName("media")
    @Expose
    var media: List<Medium>? = null) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Result> = object : Parcelable.Creator<Result> {
            override fun createFromParcel(source: Parcel): Result = Result(source)
            override fun newArray(size: Int): Array<Result?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this (
    source.createTypedArrayList(Medium.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(media)
    }
}