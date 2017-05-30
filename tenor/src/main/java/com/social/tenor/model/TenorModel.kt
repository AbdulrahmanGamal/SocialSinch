package com.social.tenor.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TenorModel (
    @SerializedName("results")
    @Expose
    var results: List<Result>? = null,
    @SerializedName("next")
    @Expose
    var next: String? = null) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<TenorModel> = object : Parcelable.Creator<TenorModel> {
            override fun createFromParcel(source: Parcel): TenorModel = TenorModel(source)
            override fun newArray(size: Int): Array<TenorModel?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.createTypedArrayList(Result.CREATOR),
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(results)
        dest.writeString(next)
    }
}