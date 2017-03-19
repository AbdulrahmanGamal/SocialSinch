package com.social.backendless.data;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by valgood on 2/14/2017.
 */

public class DataManager {

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
                                subscriber.onNext(backendlessUser);
                            }

                            @Override
                            public void handleFault(BackendlessFault backendlessFault) {
                                subscriber.onNext(backendlessFault);
                            }
                        }, keepLogged);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * Observable to obtain all the users registered in the backend
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
}
