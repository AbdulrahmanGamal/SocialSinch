package com.social.tenor.data;

import com.social.tenor.model.TenorModel;
import com.social.tenor.service.ServiceFactory;
import com.social.tenor.service.TenorService;

import rx.Observable;

/**
 * Class to handle all the operations performed with Tenor API
 */
public class DataManager {
    private static final String TAG = "DataManager";

    public static Observable<TenorModel> searchGiphyByKeyword(String keyword, int limit, String pos) {
        TenorService service =
                ServiceFactory.createRetrofitService(TenorService.class, TenorService.SERVICE_ENDPOINT);

        return service.searchGif(keyword);
    }

    public static Observable<TenorModel> getTrending(int limit, String pos) {
        TenorService service =
                ServiceFactory.createRetrofitService(TenorService.class, TenorService.SERVICE_ENDPOINT);

        return service.trending(String.valueOf(limit), pos);
    }
}
