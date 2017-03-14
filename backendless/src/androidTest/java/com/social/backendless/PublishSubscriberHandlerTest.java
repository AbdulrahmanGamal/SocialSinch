package com.example.jorgevalbuena.backendless;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.social.backendless.PublishSubscribeHandler;
import com.social.backendless.data.DataManager;
import com.social.backendless.model.ChatStatus;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test connection to backendless and send/receive messages
 */
@RunWith(AndroidJUnit4.class)
public class PublishSubscriberHandlerTest {

    private Context mMockContext;
    private static final String TAG = "PublishSubscriberHandlerTest";


    @Before
    public void setUp() {
        mMockContext = InstrumentationRegistry.getTargetContext();
        Backendless.initApp(mMockContext, "67B8DFF8-281D-7293-FF34-E2B84A032F00",
                "91A4FF6A-01C4-C388-FFF1-9389DC345F00", "v1");
    }

    @After
    public void tearDown() {
        // Empty for now
    }

    @Test
    public void publishMessage() throws InterruptedException {
        Object result = DataManager.getLoginObservable("condorito@gmail.com", "123456789", true).toBlocking().first();
        if (result instanceof BackendlessUser) {
            PublishSubscribeHandler handler = PublishSubscribeHandler.getInstance(mMockContext);
            handler.attachToChannel();
            //send message to the same user
            handler.processOutgoingMessage("8E4B12A7-2B39-778B-FF00-9715DF18DA00", "hello", ChatStatus.SEND);
            //wait while we receive the message back
            Thread.sleep(5000);
            long totalMessages = handler.getTotalMessages("8E4B12A7-2B39-778B-FF00-9715DF18DA00:8E4B12A7-2B39-778B-FF00-9715DF18DA00");
            Assert.assertTrue("Messages were not saved correctly", totalMessages >= 2);
        } else {
            System.out.println("Error logging the user: " + result);
        }

    }
}
