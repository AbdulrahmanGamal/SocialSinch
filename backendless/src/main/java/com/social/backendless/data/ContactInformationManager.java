package com.social.backendless.data;

import android.content.Context;
import android.util.Log;

import com.social.backendless.database.ChatBriteDataSource;
import com.social.backendless.model.UserInfo;
import com.social.backendless.utils.DateUtils;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Keeps the local DB consistent with the contacts information
 */

public class ContactInformationManager {
    private ChatBriteDataSource mDataSource;

    private static final String TAG = "ContactInformationManag";

    public ContactInformationManager(Context context) {
        this.mDataSource = ChatBriteDataSource.getInstance(context);
    }

    public void verifyContactInformationChanges(List<UserInfo> contacts) {
        Observable.from(contacts).filter(new Func1<UserInfo, Boolean>() {
            @Override
            public Boolean call(UserInfo userInfo) {
                String modified = mDataSource.getLastModifiedDateByContactId(userInfo.getObjectId());
                return modified == null || !userInfo.getModifiedDate().equals(modified);
            }
        }).subscribe(new Subscriber<UserInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "Error validating contact with local DB: " + e);
            }

            @Override
            public void onNext(UserInfo userInfo) {
                mDataSource.addContactInformation(userInfo);
            }
        });
    }
}
