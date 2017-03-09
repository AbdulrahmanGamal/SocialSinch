package com.social.sinchservice;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

/**
 * Created by jorgevalbuena on 3/9/17.
 */
@RunWith(AndroidJUnit4.class)
public class SinchClientTest {

    private Context mMockContext;

    @Before
    public void setUp() {
        mMockContext = InstrumentationRegistry.getTargetContext();
    }

    @After
    public void tearDown() {
        // Empty for now
    }

    @Test
    public void connectClient() throws TimeoutException {
        final String APP_KEY = "1449b242-7764-4e78-9866-34b50bb52dba";
        final String APP_SECRET = "PAlx4YPGrky4iS3eD3Wnag==";
        final String ENVIRONMENT = "sandbox.sinch.com";
        final String userLogged = "0280CFFE-6C36-D6F2-FFF1-6BF559C87900";
        SinchClient sinchClient = Sinch.getSinchClientBuilder()
                .context(mMockContext)
                .userId(userLogged)
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost(ENVIRONMENT)
                .build();
        //this client listener requires that you define
        //a few methods below
        sinchClient.addSinchClientListener(new SinchClientListener() {
            @Override
            public void onClientStarted(SinchClient sinchClient) {
                sinchClient.startListeningOnActiveConnection();
                ServiceConnectionManager.getInstance(mMockContext, userLogged);
            }

            @Override
            public void onClientStopped(SinchClient sinchClient) {

            }

            @Override
            public void onClientFailed(SinchClient sinchClient, SinchError sinchError) {

            }

            @Override
            public void onRegistrationCredentialsRequired(SinchClient sinchClient, ClientRegistration clientRegistration) {

            }

            @Override
            public void onLogMessage(int i, String s, String s1) {

            }
        });
        sinchClient.setSupportMessaging(true);
        sinchClient.setSupportActiveConnectionInBackground(true);
        sinchClient.checkManifest();
        sinchClient.start();
    }
}
