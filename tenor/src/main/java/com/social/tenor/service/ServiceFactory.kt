package com.social.tenor.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Factory to get a generic Service
 */

/**
 * Creates a retrofit service from an arbitrary class (clazz)
 * @param clazz Java interface of the retrofit service
 * *
 * @return retrofit service with defined endpoint
 */
fun <T> Class<T>.createRetrofitService(servicePoint: String, apiKey: String): T =
        Retrofit.Builder().apply {
            addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            addConverterFactory(GsonConverterFactory.create())
            baseUrl(servicePoint)
            client(getClient(apiKey))
        }.build().create(this)
/**
 * Adds the service Public Beta key as a query param
 * @return
 */
private fun getClient(apiKey: String = "") =
        OkHttpClient.Builder().apply {
            addInterceptor { chain ->
                val modifiedURL = chain.request().url().newBuilder().apply {
                    addQueryParameter("key", apiKey)
                }.build()
                chain.proceed(chain.request().newBuilder().url(modifiedURL).build())
            }.build()
        }.build()