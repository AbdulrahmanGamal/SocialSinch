package com.valgoodchat.giphy.service

import com.valgoodchat.giphy.model.GiphyModel

import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Configuration used to get GIF from giphy API
 */

interface GiphyService {

    @GET("stickers/search?")
    fun searchGif(@Query("q") keyword: String, @Query("limit") limit: String,
                  @Query("offset") offset: String, @Query("lang") lang: String): Observable<GiphyModel>

    @GET("gifs/trending?")
    fun trending(@Query("limit") limit: String, @Query("offset") offset: String): Observable<GiphyModel>
}
