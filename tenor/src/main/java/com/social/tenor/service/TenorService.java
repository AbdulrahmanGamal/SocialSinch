package com.social.tenor.service;

import com.social.tenor.model.TenorModel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Configuration used to get GIF from Tenor API
 */

public interface TenorService {
    String SERVICE_ENDPOINT = "https://api.tenor.co/v1/";

    @GET("search?")
    Observable<TenorModel> searchGif(@Query("limit") String offset,
                                     @Query("pos") String pos,
                                     @Query("tag") String keyword,
                                     @Query("locale") String locale);

    @GET("trending?")
    Observable<TenorModel> trending(@Query("limit") String offset, @Query("pos") String pos);

}
