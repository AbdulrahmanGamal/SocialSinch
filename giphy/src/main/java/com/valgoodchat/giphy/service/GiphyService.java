package com.valgoodchat.giphy.service;

import com.valgoodchat.giphy.model.GiphyModel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Configuration used to get GIF from giphy API
 */

public interface GiphyService {
    String SERVICE_ENDPOINT = "https://api.tenor.co/v1/";

    @GET("search?")
    Observable<GiphyModel> searchGif(@Query("tag") String keyword);

    @GET("trending?")
    Observable<GiphyModel> trending(@Query("limit") String offset);
}
