package com.vanniktech.emoji.data;

import android.content.Context;
import com.vanniktech.emoji.database.EmoticonBriteDataSource;
import java.util.List;

/**
 * Manager to comunicate with the local database to obtain or save recent animated emoticons
 */
public class EmoticonDataManager {
    private EmoticonBriteDataSource mEmoticonBriteData;
    private static EmoticonDataManager sEmoticonDataManager;

    public static EmoticonDataManager getInstance(Context context) {
        if (sEmoticonDataManager == null) {
            sEmoticonDataManager = new EmoticonDataManager(context);
        }

        return sEmoticonDataManager;
    }

    private EmoticonDataManager(Context context) {
        mEmoticonBriteData = EmoticonBriteDataSource.getInstance(context);
    }

    public List<String> getRecent() {
        return mEmoticonBriteData.getRecent();
    }

    public void addRecent(String emoticonURL) {
        mEmoticonBriteData.addRecent(emoticonURL);
    }

    public int getTotalRecent() {
        return mEmoticonBriteData.getTotalRecent();
    }
}
