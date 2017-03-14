package com.social.sinchservice;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.backendless.Backendless;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.messaging.WritableMessage;
import java.util.concurrent.Callable;

import rx.Emitter;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Class to encapsulate all the connection with the sinch client
 */

public class SinchClientHandler {
    private static final String APP_KEY = "1449b242-7764-4e78-9866-34b50bb52dba";
    private static final String APP_SECRET = "PAlx4YPGrky4iS3eD3Wnag==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";

    private static final String TAG = "SinchClientHandler";
    private SinchClientMessageListener mMessageListener;
    private SinchClient mSinchClient;
    private Intent broadcastIntent = new Intent("com.parse.sinch.social.SinchStatus");
    private LocalBroadcastManager broadcaster;

    private PublishMessage mPublishMessage;

    public SinchClientHandler(Context context) {
        //bound to the service to have the singleton always bound for all views
        SinchServiceConnection.getInstance().bindToService(context);
        this.broadcaster = LocalBroadcastManager.getInstance(context);
        this.mPublishMessage = new PublishMessage();
    }
    /**
     * Initiates the sinch client to start listening for messages, calls, etc
     * @param context
     * @param userLogged
     * @return
     */
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

                addSinchClientListenerWrapper(sinchClient);
                mMessageListener = new SinchClientMessageListener(context, userLogged);
                mMessageListener.addMessageClientListenerWrapper(sinchClient.getMessageClient());
                sinchClient.setSupportMessaging(true);
                sinchClient.setSupportActiveConnectionInBackground(true);
                sinchClient.checkManifest();
                sinchClient.start();
                Looper.loop();
                return sinchClient;
            }
        }).subscribeOn(Schedulers.computation());
    }

    /**
     * Verify is the sinch client is up and running
     * @return
     */
    public boolean isSinchClientStarted() {
        return mSinchClient != null && mSinchClient.isStarted();
    }

    /**
     * Send a message using the Sinch Client
     * @param recipientUserId
     * @param textBody
     */
    public void sendMessage(String recipientUserId, String textBody) {
        mPublishMessage.sendMessage(recipientUserId, textBody);
    }
    /**
     * Stop listening for messages and terminates the sinch client
     */
    public void terminate() {
        if (isSinchClientStarted()) {
            mSinchClient.stopListeningOnActiveConnection();
            mSinchClient.terminate();
        }
    }
    /**
     * Obtains the last messages from DB
     * @param senderId
     * @param recipientId
     * @param max
     * @return
     */
//    public List<ChatMessage> retrieveLastMessages(String senderId, String recipientId, int max) {
//        return mMessageListener.retrieveLastMessages(senderId, recipientId, max);
//    }
    /**
     * Wrapper the connection listener into an obserbable to control the connection with the sinch client
     * @param sinchClient
     */
    public void addSinchClientListenerWrapper(final SinchClient sinchClient) {
        Observable.fromEmitter(new Action1<Emitter<Void>>() {
            @Override
            public void call(Emitter<Void> emitter) {
                SinchClientListener sinchClientListener = new SinchClientListener() {
                    @Override
                    public void onClientStarted(SinchClient sinchClient) {
                        sinchClient.startListeningOnActiveConnection();
                        Log.e(TAG, "onClientStarted");
                        mSinchClient = sinchClient;
                        broadcastResult(true);
                    }

                    @Override
                    public void onClientStopped(SinchClient sinchClient) {
                        Log.e(TAG, "onClientStopped");
                        mSinchClient = null;
                        broadcastResult(false);
                    }

                    @Override
                    public void onClientFailed(SinchClient sinchClient, SinchError sinchError) {
                        Log.e(TAG, "onClientFailed");
                        mSinchClient = null;
                        broadcastResult(false);
                    }

                    @Override
                    public void onRegistrationCredentialsRequired(SinchClient sinchClient, ClientRegistration clientRegistration) {
                        Log.e(TAG, "onRegistrationCredentialsRequired");
                    }

                    @Override
                    public void onLogMessage(int i, String s, String s1) {

                    }
                };
                sinchClient.addSinchClientListener(sinchClientListener);
            }
        }, Emitter.BackpressureMode.NONE).subscribe();
    }

    private void broadcastResult(boolean status) {
        broadcastIntent.putExtra("success", status);
        broadcaster.sendBroadcast(broadcastIntent);
    }
}
