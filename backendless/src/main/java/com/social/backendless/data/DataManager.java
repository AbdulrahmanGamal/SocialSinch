package com.social.backendless.data;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.social.backendless.model.UserInfo;
import com.social.backendless.utils.DateUtils;
import com.social.backendless.utils.LoggedUser;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Class to handle all the operations performed with Backendless
 */

public class DataManager {

    private static final String LAST_SEEN_PROPERTY = "last_seen";
    private static final String TAG = "DataManager";

    /**
     * Observable to sign in the user with the backend
     * @param login
     * @param password
     * @param keepLogged
     * @return
     */
    public static Observable<Object> getLoginObservable(final String login,
                                         final String password,
                                         final boolean keepLogged) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(final Subscriber<? super Object> subscriber) {
                Backendless.UserService.login(login,
                        password, new  AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser backendlessUser) {
                                LoggedUser currentUser = LoggedUser.getInstance();
                                currentUser.setUserLogged(backendlessUser);
                                currentUser.setUserLoggedId(backendlessUser.getUserId());
                                subscriber.onNext(backendlessUser);
                            }

                            @Override
                            public void handleFault(BackendlessFault backendlessFault) {
                                LoggedUser.getInstance().setUserLogged(null);
                                subscriber.onNext(backendlessFault);
                            }
                        }, keepLogged);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * Observable to obtain all the users registered in the backend but the user logged
     * @return
     */
    public static Observable<Object> getFetchAllUsersObservable(final String subscriberId) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(final Subscriber<? super Object> subscriber) {
                String whereClause = "objectId != '" + subscriberId + "'";
                BackendlessDataQuery dataQuery = new BackendlessDataQuery();
                dataQuery.setWhereClause( whereClause );
                Backendless.Data.of(BackendlessUser.class).find(dataQuery, new AsyncCallback<BackendlessCollection<BackendlessUser>>() {
                    @Override
                    public void handleResponse(BackendlessCollection<BackendlessUser> backendlessUserBackendlessCollection) {
                        subscriber.onNext(backendlessUserBackendlessCollection);
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        subscriber.onNext(backendlessFault);
                    }
                });
            }
        }).subscribeOn(Schedulers.io());
    }
    /**
     * Update the last seen filed in the remote sever
     */
    public static void updateLastSeenFieldInRemote() {
        BackendlessUser currentUser = LoggedUser.getInstance().getUserLogged();
        if (currentUser != null) {
            Date lastSeenDate = Calendar.getInstance(Locale.getDefault()).getTime();
            currentUser.setProperty(LAST_SEEN_PROPERTY, lastSeenDate);
            Backendless.UserService.update(currentUser, new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser response) {
                    Log.d(TAG, "LastSeen Time updated SuccessFully!!");
                    LoggedUser.getInstance().setUserLogged(response);
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Log.e(TAG, "LastSeen Time Failed: " + fault.toString());
                }
            });
        }
    }

    /**
     * Obtains the latest information registered in backendless for a specific user.
     * @param userId
     * @return
     */
    public static Observable<UserInfo> getUserInformationObservable(final String userId) {
        return Observable.create(new Observable.OnSubscribe<UserInfo>() {
            @Override
            public void call(final Subscriber<? super UserInfo> subscriber) {
                Backendless.UserService.findById(userId, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        subscriber.onNext(convertToUserInfo(response));
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        subscriber.onNext(new UserInfo());
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).share();
    }

    /**
     * Transform a backendless user object to UserInfo object recognized by the UI
     * @param backendlessUser
     * @return
     */
    private static UserInfo convertToUserInfo(BackendlessUser backendlessUser) {

        UserInfo user = new UserInfo();
        user.setObjectId(backendlessUser.getObjectId());
        user.setFullName((String) backendlessUser.getProperty("full_name"));
        user.setPhoneNumber((String) backendlessUser.getProperty("phone"));
        user.setProfilePicture((String) backendlessUser.getProperty("avatar"));
        Date lastTimeSeen = (Date) backendlessUser.getProperty("last_seen");
        Date lastLogin = (Date) backendlessUser.getProperty("lastLogin");
        if (lastTimeSeen == null) {
            user.setLastSeen(DateUtils.convertDateToString(lastLogin));
        } else {
            user.setLastSeen(DateUtils.convertDateToString(lastTimeSeen));
        }
        return user;
    }
}
