package com.social.tenor.data

import com.social.tenor.model.TenorModel
import com.social.tenor.service.TenorService
import com.social.tenor.service.createRetrofitService

import rx.Observable

/**
 * Class to handle all the operations performed with Tenor API
 */
object DataManager {
    private val API_KEY = "LIVDSRZULELA"
    private val SERVICE_ENDPOINT = "https://api.tenor.co/v1/"

    fun searchGiphyByKeyword(keyword: String, limit: Int, pos: String,
                             locale: String): Observable<TenorModel> =
            TenorService::class.java.createRetrofitService(SERVICE_ENDPOINT, API_KEY)
                      .searchGif(limit.toString(), pos, keyword, locale)


    fun getTrending(limit: Int, pos: String): Observable<TenorModel> =
            TenorService::class.java.createRetrofitService(SERVICE_ENDPOINT, API_KEY)
                      .trending(limit.toString(), pos)

}
