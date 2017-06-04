package com.valgoodchat.meme.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Factory to get a generic Service
 */

object ServiceFactory {
    private val SERVICE_ENDPOINT = "https://api.imgflip.com/"
    /**
     * Creates a retrofit service from an arbitrary class (clazz)
     * @param clazz Java interface of the retrofit service
     * *
     * @param endPoint REST endpoint url
     * *
     * @return retrofit service with defined endpoint
     */
    fun <T> createRetrofitService(clazz: Class<T>): T {
        val restAdapter = Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SERVICE_ENDPOINT)
                .client(OkHttpClient())
                .build()
        return restAdapter.create(clazz)
    }
}
