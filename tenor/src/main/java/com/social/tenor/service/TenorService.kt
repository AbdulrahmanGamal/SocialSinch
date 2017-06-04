package com.social.tenor.service

import com.social.tenor.model.TenorModel

import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Configuration used to get GIF from Tenor API
 */

interface TenorService {

    @GET("search?")
    fun searchGif(@Query("limit") offset: String,
                  @Query("pos") pos: String,
                  @Query("tag") keyword: String,
                  @Query("locale") locale: String): Observable<TenorModel>

    @GET("trending?")
    fun trending(@Query("limit") offset: String, @Query("pos") pos: String): Observable<TenorModel>
}
