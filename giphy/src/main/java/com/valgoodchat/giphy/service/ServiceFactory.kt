package com.valgoodchat.giphy.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Factory to get a generic Service
 */

object ServiceFactory {
    private val API_KEY = "dc6zaTOxFJmzC"
    /**
     * Creates a retrofit service from an arbitrary class (clazz)
     * @param clazz Java interface of the retrofit service
     * *
     * @param endPoint REST endpoint url
     * *
     * @return retrofit service with defined endpoint
     */
    fun <T> createRetrofitService(clazz: Class<T>, endPoint: String): T {
        val restAdapter = Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(endPoint)
                .client(httpClient)
                .build()
        return restAdapter.create(clazz)
    }

    /**
     * Adds the giphy Public Beta key as a query param
     * @return
     */
    private // Request customization: add request headers
    val httpClient: OkHttpClient
        get() {
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url()

                val url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", API_KEY)
                        .build()
                val requestBuilder = original.newBuilder()
                        .url(url)

                val request = requestBuilder.build()
                chain.proceed(request)
            }

            return httpClient.build()
        }
}
