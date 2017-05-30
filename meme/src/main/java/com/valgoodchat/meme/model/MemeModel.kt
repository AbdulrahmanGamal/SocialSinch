package com.valgoodchat.meme.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MemeModel(

    @SerializedName("success")
    @Expose
    var isSuccess: Boolean = false,
    @SerializedName("data")
    @Expose
    var data: Data? = null)
