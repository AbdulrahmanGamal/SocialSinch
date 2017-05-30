package com.valgoodchat.meme.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data (
    @SerializedName("memes")
    @Expose
    var memes: List<Meme>? = null)
