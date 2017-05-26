package com.valgoodchat.meme.service;

import com.valgoodchat.meme.model.MemeModel;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Configuration used to get GIF from giphy API
 */

public interface MemeService {
    String SERVICE_ENDPOINT = "https://api.imgflip.com/";

    @GET("get_memes")
    Observable<MemeModel> trending();
}
