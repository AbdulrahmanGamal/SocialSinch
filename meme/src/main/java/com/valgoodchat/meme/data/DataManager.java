package com.valgoodchat.meme.data;

import com.valgoodchat.meme.model.MemeModel;
import com.valgoodchat.meme.service.MemeService;
import com.valgoodchat.meme.service.ServiceFactory;

import rx.Observable;

/**
 * Class to handle all the operations performed with Imgflip API
 */
public class DataManager {
    private static final String TAG = "DataManager";

    public static Observable<MemeModel> getTrending() {
        MemeService service =
                ServiceFactory.createRetrofitService(MemeService.class, MemeService.SERVICE_ENDPOINT);

        return service.trending();
    }
}
