package com.social.sinchservice;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jorgevalbuena on 3/10/17.
 */

public class SinchClientHandler {
    static final String APP_KEY = "1449b242-7764-4e78-9866-34b50bb52dba";
    static final String APP_SECRET = "PAlx4YPGrky4iS3eD3Wnag==";
    static final String ENVIRONMENT = "sandbox.sinch.com";

    private static final String TAG = "SinchClientHandler";
    private static SinchClient mSinchClient;
    private Intent broadcastIntent = new Intent("com.parse.sinch.social.TabActivity");
    private LocalBroadcastManager broadcaster;
    private MessageClient messageClient = null;

    public SinchClientHandler(Context context) {
        this.broadcaster = LocalBroadcastManager.getInstance(context);
    }
    public static SinchClient getInstance(Context context, String userLogged) {
        if (mSinchClient == null) {
            //mSinchClient = SinchClientHandler.startSinchClient(context, userLogged).toBlocking().first();
        }

        return mSinchClient;
    }

    private SinchClientListener mSinchClientListener = new SinchClientListener() {
        @Override
        public void onClientStarted(SinchClient sinchClient) {
            sinchClient.startListeningOnActiveConnection();
            Log.e(TAG, "onClientStarted");
            messageClient = sinchClient.getMessageClient();

            broadcastIntent.putExtra("success", true);
            broadcaster.sendBroadcast(broadcastIntent);
            //addMessageClientListener(sinchClient);
            //ServiceConnectionManager.getInstance(context, userLogged);
        }

        @Override
        public void onClientStopped(SinchClient sinchClient) {
            Log.e(TAG, "onClientStopped");
        }

        @Override
        public void onClientFailed(SinchClient sinchClient, SinchError sinchError) {
            Log.e(TAG, "onClientFailed");
        }

        @Override
        public void onRegistrationCredentialsRequired(SinchClient sinchClient, ClientRegistration clientRegistration) {
            Log.e(TAG, "onRegistrationCredentialsRequired");
        }

        @Override
        public void onLogMessage(int i, String s, String s1) {
            Log.e(TAG, "onLogMessage : " + s + " ***  " + s1);
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
                sinchClient.getMessageClient().addMessageClientListener(new MessageClientListener() {
                    @Override
                    public void onIncomingMessage(MessageClient messageClient, Message message) {
                        Log.e(TAG, "onIncomingMessage : " + " ***  " + message.getTextBody());
                    }

                    @Override
                    public void onMessageSent(MessageClient messageClient, Message message, String s) {
                        Log.e(TAG, "onIncomingMessage : " + " ***  " + message.getTextBody());
                    }

                    @Override
                    public void onMessageFailed(MessageClient messageClient, Message message, MessageFailureInfo messageFailureInfo) {
                        Log.e(TAG, "onIncomingMessage : " + " ***  " + message.getTextBody());
                    }

                    @Override
                    public void onMessageDelivered(MessageClient messageClient, MessageDeliveryInfo messageDeliveryInfo) {
                    }

                    @Override
                    public void onShouldSendPushData(MessageClient messageClient, Message message, List<PushPair> list) {

                    }
                });
                sinchClient.setSupportMessaging(true);
                sinchClient.setSupportActiveConnectionInBackground(true);
                sinchClient.checkManifest();
                sinchClient.start();
                Looper.loop();
                return sinchClient;
            }
        }).subscribeOn(Schedulers.computation());
    }

    public Observable<String> observableMessageClientListenerWrapper(final MessageClient messageClient) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                MessageClientListener messageClientListener = new MessageClientListener() {
                        @Override
                        public void onIncomingMessage(MessageClient messageClient, Message message) {
                            Log.e(TAG, "onIncomingMessage : " + " ***  " + message.getTextBody());
                            if (subscriber.isUnsubscribed()) {
                                messageClient.removeMessageClientListener(this);
                            } else {
                                subscriber.onNext("onIncomingMessage : " + " ***  " + message.getTextBody());
                            }
                        }

                        @Override
                        public void onMessageSent(MessageClient messageClient, Message message, String s) {
                            Log.e(TAG, "onMessageSent : " + s + " ***  " + s);
                            if (subscriber.isUnsubscribed()) {
                                messageClient.removeMessageClientListener(this);
                            } else {
                                subscriber.onNext("onMessageSent : " + s + " ***  " + s);
                            }
                        }

                        @Override
                        public void onMessageFailed(MessageClient messageClient, Message message, MessageFailureInfo messageFailureInfo) {

                        }

                        @Override
                        public void onMessageDelivered(MessageClient messageClient, MessageDeliveryInfo messageDeliveryInfo) {
                            Log.e(TAG, "onMessageDelivered : " + messageDeliveryInfo.getMessageId());
                            if (subscriber.isUnsubscribed()) {
                                messageClient.removeMessageClientListener(this);
                            } else {
                                subscriber.onNext("onMessageDelivered : " + messageDeliveryInfo.getMessageId());
                            }
                        }

                        @Override
                        public void onShouldSendPushData(MessageClient messageClient, Message message, List<PushPair> list) {

                        }
                    };

                messageClient.addMessageClientListener(messageClientListener);
            }
        });
    }

    public void combineObservables(Context context, String userLogged) {
//        SinchClientHandler.startSinchClient(context, userLogged).concatMap(new Func1<SinchClient, Observable<String>>() {
//            @Override
//            public Observable<String> call(SinchClient sinchClient) {
//                Log.e(TAG, "adding MessageListener to Message CLIENT!! : ");
//                return observableMessageClientListenerWrapper(sinchClient.getMessageClient());
//            }
//        }).subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                Log.e(TAG, "Action executed: " + s);
//            }
//        });
    }
    public void removeSinchClientListener() {
        mSinchClient.removeSinchClientListener(mSinchClientListener);
    }
//    private void addMessageClientListener(SinchClient sinchClient) {
//        sinchClient.getMessageClient().addMessageClientListener();
//    }
}
