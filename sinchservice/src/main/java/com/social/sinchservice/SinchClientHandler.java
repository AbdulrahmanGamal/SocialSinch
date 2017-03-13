package com.social.sinchservice;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.messaging.WritableMessage;
import com.social.sinchservice.model.ChatMessage;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by jorgevalbuena on 3/10/17.
 */

public class SinchClientHandler {
    static final String APP_KEY = "1449b242-7764-4e78-9866-34b50bb52dba";
    static final String APP_SECRET = "PAlx4YPGrky4iS3eD3Wnag==";
    static final String ENVIRONMENT = "sandbox.sinch.com";

    private static final String TAG = "SinchClientHandler";
    private SinchClientMessageListener mMessageListener;
    private SinchClient mSinchClient;

    private SinchClientListener mSinchClientListener = new SinchClientListener() {
        @Override
        public void onClientStarted(SinchClient sinchClient) {
            sinchClient.startListeningOnActiveConnection();
            Log.e(TAG, "onClientStarted");
            mSinchClient = sinchClient;
        }

        @Override
        public void onClientStopped(SinchClient sinchClient) {
            Log.e(TAG, "onClientStopped");
            mSinchClient = null;
        }

        @Override
        public void onClientFailed(SinchClient sinchClient, SinchError sinchError) {
            Log.e(TAG, "onClientFailed");
            mSinchClient = null;
        }

        @Override
        public void onRegistrationCredentialsRequired(SinchClient sinchClient, ClientRegistration clientRegistration) {
            Log.e(TAG, "onRegistrationCredentialsRequired");
        }

        @Override
        public void onLogMessage(int i, String s, String s1) {
            //Log.e(TAG, "onLogMessage : " + s + " ***  " + s1);
        }
    };

    public Observable<SinchClient> startSinchClient(final Context context, final String userLogged) {
        return Observable.fromCallable(new Callable<SinchClient>() {
            @Override
            public SinchClient call() throws Exception {
                Looper.prepare();
                SinchClient sinchClient = Sinch.getSinchClientBuilder()
                        .context(context)
                        .userId(userLogged)
                        .applicationKey(APP_KEY)
                        .applicationSecret(APP_SECRET)
                        .environmentHost(ENVIRONMENT)
                        .build();

                sinchClient.addSinchClientListener(mSinchClientListener);
                mMessageListener = new SinchClientMessageListener(context, userLogged);
                mMessageListener.observableMessageClientListenerWrapper(sinchClient.getMessageClient());
                sinchClient.setSupportMessaging(true);
                sinchClient.setSupportActiveConnectionInBackground(true);
                sinchClient.checkManifest();
                sinchClient.start();
                Looper.loop();
                return sinchClient;
            }
        }).subscribeOn(Schedulers.computation());
    }

    public boolean isSinchClientStarted() {
        return mSinchClient != null && mSinchClient.isStarted();
    }

    public void sendMessage(String recipientUserId, String textBody) {
        if (isSinchClientStarted()) {
            WritableMessage message = new WritableMessage(recipientUserId, textBody);
            mSinchClient.getMessageClient().send(message);
        }
    }

    public void terminate() {
        if (isSinchClientStarted()) {
            mSinchClient.stopListeningOnActiveConnection();
            mSinchClient.terminate();
        }
    }

    public List<ChatMessage> retrieveLastMessages(String senderId, String recipientId, int max) {
        return mMessageListener.retrieveLastMessages(senderId, recipientId, max);
    }
}
