package com.social.tenor.data

import com.social.tenor.model.TenorModel
import com.social.tenor.service.ServiceFactory
import com.social.tenor.service.TenorService

import rx.Observable

/**
 * Class to handle all the operations performed with Tenor API
 */
object DataManager {
    private val TAG = "DataManager"

    fun searchGiphyByKeyword(keyword: String,
                             limit: Int,
                             pos: String,
                             locale: String): Observable<TenorModel> {
        val service = ServiceFactory.createRetrofitService(TenorService::class.java, TenorService.SERVICE_ENDPOINT)

        return service.searchGif(limit.toString(), pos, keyword, locale)
    }

    fun getTrending(limit: Int, pos: String): Observable<TenorModel> {
        val service = ServiceFactory.createRetrofitService(TenorService::class.java, TenorService.SERVICE_ENDPOINT)

        return service.trending(limit.toString(), pos)
    }
}
