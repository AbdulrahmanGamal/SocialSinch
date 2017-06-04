package com.valgoodchat.giphy.data

import com.valgoodchat.giphy.model.GiphyModel
import com.valgoodchat.giphy.service.GiphyService
import com.valgoodchat.giphy.service.ServiceFactory

import rx.Observable

/**
 * Class to handle all the operations performed with Giphy API
 */
object DataManager {
    fun searchGiphyByKeyword(keyword: String,
                             limit: Int,
                             pos: String,
                             locale: String): Observable<GiphyModel> {
        val service = ServiceFactory.createRetrofitService(GiphyService::class.java)

        return service.searchGif(keyword, pos, limit.toString(), locale)
    }

    fun getTrending(limit: Int, pos: String): Observable<GiphyModel> {
        val service = ServiceFactory.createRetrofitService(GiphyService::class.java)

        return service.trending(limit.toString(), pos)
    }
}
