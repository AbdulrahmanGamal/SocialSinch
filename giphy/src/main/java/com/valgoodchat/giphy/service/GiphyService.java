package com.valgoodchat.giphy.service;

import com.valgoodchat.giphy.model.GiphyModel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Configuration used to get GIF from giphy API
 */

public interface GiphyService {
    String SERVICE_ENDPOINT = "https://api.giphy.com/v1/";

    @GET("stickers/search?")
    Observable<GiphyModel> searchGif(@Query("q") String keyword, @Query("limit") String limit,
                                     @Query("offset") String offset, @Query("lang") String lang);

    @GET("gifs/trending?")
    Observable<GiphyModel> trending(@Query("limit") String limit, @Query("offset") String offset);
}
