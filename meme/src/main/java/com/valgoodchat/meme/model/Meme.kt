package com.valgoodchat.meme.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Meme (
    @SerializedName("id")
    @Expose
    var id: String? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("url")
    @Expose
    var url: String? = null,
    @SerializedName("width")
    @Expose
    var width: Int = 0,
    @SerializedName("height")
    @Expose
    var height: Int = 0)
