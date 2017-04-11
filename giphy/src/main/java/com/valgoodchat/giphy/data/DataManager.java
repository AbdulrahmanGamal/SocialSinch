package com.valgoodchat.giphy.data;

import com.valgoodchat.giphy.model.GiphyModel;
import com.valgoodchat.giphy.service.GiphyService;
import com.valgoodchat.giphy.service.ServiceFactory;

import rx.Observable;

/**
 * Class to handle all the operations performed with Giphy API
 */
public class DataManager {
    private static final String TAG = "DataManager";

    public static Observable<GiphyModel> searchGiphyByKeyword(String keyword) {
        GiphyService service =
            ServiceFactory.createRetrofitService(GiphyService.class, GiphyService.SERVICE_ENDPOINT);

        return service.searchGif(keyword);
    }

    public static Observable<GiphyModel> getTrending(int limit) {
        GiphyService service =
                ServiceFactory.createRetrofitService(GiphyService.class, GiphyService.SERVICE_ENDPOINT);

        return service.trending(String.valueOf(limit));
    }
}
