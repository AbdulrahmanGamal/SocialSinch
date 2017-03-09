package com.social;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.social.sinchservice.SinchService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.core.Is.is;

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

        // Create the service Intent.
        final Intent serviceIntent =
                new Intent(mMockContext,
                        SinchService.class);

        // Data can be passed to the service via the Intent.
        serviceIntent.putExtra(SinchService.CURRENT_USER_KEY, userLogged);
        mServiceRule.startService(serviceIntent);

        // Bind the service and grab a reference to the binder.
        IBinder binder = mServiceRule.bindService(serviceIntent);

        // Get the reference to the service, or you can call public methods on the binder directly.
        SinchService service = ((SinchService.MessageServiceInterface) binder).getService();

        // Verify that the service is working correctly.
        Assert.assertNotNull(service);

//        Intent intent = new Intent(mMockContext, SinchService.class);
//        intent.putExtra(SinchService.CURRENT_USER_KEY, userLogged);
//        mMockContext.startService(intent);
//
        //broadcast receiver to listen for the broadcast
        //from MessageService
//        BroadcastReceiver receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Boolean success = intent.getBooleanExtra("success", false);
//                //ServiceConnectionManager.getInstance(mMockContext, userLogged);
//                // Bind the service and grab a reference to the binder.
//                IBinder binder = null;
//                try {
//                    binder = mServiceRule.bindService(serviceIntent);
//                    // Get the reference to the service, or you can call
//                    // public methods on the binder directly.
//                    SinchService.MessageServiceInterface service =
//                            ((SinchService.MessageServiceInterface) binder);
//
//                    // Verify that the service is working correctly.
//                    Assert.assertThat(service.isSinchClientStarted(), is(true));
//                    //show a toast message if the Sinch
//                    //service failed to start
//                } catch (TimeoutException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        LocalBroadcastManager.getInstance(mMockContext).registerReceiver(receiver, new IntentFilter("com.parse.sinch.social.TabActivity"));
    }
}
