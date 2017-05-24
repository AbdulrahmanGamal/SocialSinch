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

    public static Observable<GiphyModel> searchGiphyByKeyword(String keyword,
                                                              int limit,
                                                              String pos,
                                                              String locale) {
        GiphyService service =
            ServiceFactory.createRetrofitService(GiphyService.class, GiphyService.SERVICE_ENDPOINT);

        return service.searchGif(keyword, pos, String.valueOf(limit), locale);
    }

    public static Observable<GiphyModel> getTrending(int limit, String pos) {
        GiphyService service =
                ServiceFactory.createRetrofitService(GiphyService.class, GiphyService.SERVICE_ENDPOINT);

        return service.trending(String.valueOf(limit), pos);
    }
}
