package com.social.backendless.utils;

import android.content.Context;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

/**
 * Class to handle backendless calls
 */

public class ApplicationUtils {

    private static String SENDER_ID_PUSH = "452090331547";
    private static final String TAG = "ApplicationUtils";

    public static void init(final Context context) {
        Backendless.initApp(context, "67B8DFF8-281D-7293-FF34-E2B84A032F00",
                "91A4FF6A-01C4-C388-FFF1-9389DC345F00", "v1");
        registerDeviceForPush();
    }
    private static void registerDeviceForPush() {
        Backendless.Messaging.registerDevice(SENDER_ID_PUSH, new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {
                Log.d(TAG, "Device Registered Successfully");
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "Device Registered Error: " + fault.getMessage());
            }
        });
    }
}
