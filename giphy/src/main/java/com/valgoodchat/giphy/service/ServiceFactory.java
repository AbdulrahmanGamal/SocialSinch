package com.valgoodchat.giphy.service;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Factory to get a generic Service
 */

public class ServiceFactory {
    private static final String API_KEY = "dc6zaTOxFJmzC";
    /**
     * Creates a retrofit service from an arbitrary class (clazz)
     * @param clazz Java interface of the retrofit service
     * @param endPoint REST endpoint url
     * @return retrofit service with defined endpoint
     */
    public static <T> T createRetrofitService(final Class<T> clazz, final String endPoint) {
        final Retrofit restAdapter = new Retrofit.Builder()
                                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .baseUrl(endPoint)
                                    .client(getHttpClient())
                                    .build();
        return restAdapter.create(clazz);
    }

    /**
     * Adds the giphy Public Beta key as a query param
     * @return
     */
    private static OkHttpClient getHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", API_KEY)
                    .build();

            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .url(url);

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        return httpClient.build();
    }
}
