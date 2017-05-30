package com.valgoodchat.meme.data

import com.valgoodchat.meme.model.MemeModel
import com.valgoodchat.meme.service.MemeService
import com.valgoodchat.meme.service.ServiceFactory

import rx.Observable

/**
 * Class to handle all the operations performed with Imgflip API
 */
object DataManager {
    val trending: Observable<MemeModel>
        get() {
            val service = ServiceFactory.createRetrofitService(MemeService::class.java, MemeService.SERVICE_ENDPOINT)
            return service.trending()
        }
}
