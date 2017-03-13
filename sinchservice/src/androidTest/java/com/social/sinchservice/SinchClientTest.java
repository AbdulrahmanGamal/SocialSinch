package com.social.sinchservice;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

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
    private static final String TAG = "SinchClientTest";


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
        final String userLogged = "0280CFFE-6C36-D6F2-FFF1-6BF559C87900";
        final SinchClientHandler sinchClientHandler = new SinchClientHandler();
        sinchClientHandler.startSinchClient(mMockContext, userLogged).toBlocking().first();
    }
}
