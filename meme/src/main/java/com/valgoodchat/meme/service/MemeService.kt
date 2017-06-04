package com.valgoodchat.meme.service

import com.valgoodchat.meme.model.MemeModel

import retrofit2.http.GET
import rx.Observable

/**
 * Configuration used to get GIF from ImgFlip API
 */

interface MemeService {
    @GET("get_memes")
    fun trending(): Observable<MemeModel>
}
