package com.social;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by jorgevalbuena on 3/9/17.
 */
@RunWith(AndroidJUnit4.class)
public class SinchServiceTest {
    private Context mMockContext;

    @Rule
    public final ServiceTestRule mServiceRule = ServiceTestRule.withTimeout(10000, TimeUnit.MILLISECONDS);

    @Before
    public void setUp() {
        mMockContext = InstrumentationRegistry.getTargetContext();
    }

    @After
    public void tearDown() {
        // Empty for now
    }

    @Test
    public void connectService() throws TimeoutException {
        final String userLogged = "0280CFFE-6C36-D6F2-FFF1-6BF559C87900";

//        // Create the service Intent.
//        final Intent serviceIntent =
//                new Intent(mMockContext,
//                        SinchService.class);
//
//        // Data can be passed to the service via the Intent.
//        serviceIntent.putExtra(SinchService.CURRENT_USER_KEY, userLogged);
//        mServiceRule.startService(serviceIntent);
//
//        // Bind the service and grab a reference to the binder.
//        IBinder binder = mServiceRule.bindService(serviceIntent);
//
//        // Get the reference to the service, or you can call public methods on the binder directly.
//        SinchService.MessageServiceInterface service = ((SinchService.MessageServiceInterface) binder);
//
//        // Verify that the service is working correctly.
//        Assert.assertNotNull(service);
    }
}
