package com.social.sinchservice;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by jorgevalbuena on 3/9/17.
 */
@RunWith(AndroidJUnit4.class)
public class SinchClientTest {

    private Context mMockContext;
    private static final String TAG = "SinchClientTest";
    private SinchClient client;
    private SinchMessageClientListener sinchMessageClientListener;

    @Before
    public void setUp() {
        mMockContext = InstrumentationRegistry.getTargetContext();
        //client = SinchClientHandler.getInstance(mMockContext, "0280CFFE-6C36-D6F2-FFF1-6BF559C87900");

    }

    @After
    public void tearDown() {
        // Empty for now
        SinchClientHandler.removeSinchClientListener();
    }

    @Test
    public void connectClient() throws TimeoutException {
        final String userLogged = "0280CFFE-6C36-D6F2-FFF1-6BF559C87900";
        final SinchClientHandler sinchClientHandler = new SinchClientHandler();
        //sinchClientHandler.combineObservables(mMockContext, userLogged);
        sinchClientHandler.startSinchClient(mMockContext, userLogged).concatMap(new Func1<SinchClient, Observable<String>>() {
            @Override
            public Observable<String> call(SinchClient sinchClient) {
                Log.e(TAG, "adding MessageListener to Message CLIENT!! : ");
                return sinchClientHandler.observableMessageClientListenerWrapper(sinchClient.getMessageClient());
            }
        }).toBlocking().first();
//        final SinchClient client = sinchClientHandler.startSinchClient(mMockContext, userLogged).toBlocking().first();

//        Observable.fromCallable(new Callable<Object>() {
//            @Override
//            public Object call() throws Exception {
//                client.getMessageClient().addMessageClientListener(new MessageClientListener() {
//                    @Override
//                    public void onIncomingMessage(MessageClient messageClient, Message message) {
//                        Log.e(TAG, "onIncomingMessage : " + " ***  " + message.getTextBody());
//                    }
//
//                    @Override
//                    public void onMessageSent(MessageClient messageClient, Message message, String s) {
//                        Log.e(TAG, "onMessageSent : " + s + " ***  " + s);
//                    }
//
//                    @Override
//                    public void onMessageFailed(MessageClient messageClient, Message message, MessageFailureInfo messageFailureInfo) {
//
//                    }
//
//                    @Override
//                    public void onMessageDelivered(MessageClient messageClient, MessageDeliveryInfo messageDeliveryInfo) {
//                        Log.e(TAG, "onMessageDelivered : " + messageDeliveryInfo.getMessageId());
//                    }
//
//                    @Override
//                    public void onShouldSendPushData(MessageClient messageClient, Message message, List<PushPair> list) {
//
//                    }
//                });
//
//                return null;
//            }
//        }).subscribeOn(Schedulers.computation()).toCompletable().await(20, TimeUnit.SECONDS);

        //this client listener requires that you define
        //a few methods below
    }
}
