package com.social.backendless.utils;

import android.content.Context;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

/**
 * Class to handle backendless calls
 */

public class ApplicationUtils {

    private static String SENDER_ID_PUSH = "452090331547";
    public static void init(final Context context) {
        Backendless.initApp(context, "67B8DFF8-281D-7293-FF34-E2B84A032F00",
                "91A4FF6A-01C4-C388-FFF1-9389DC345F00", "v1");
        registerDeviceForPush(context);
    }
    private static void registerDeviceForPush(final Context context) {
        Backendless.Messaging.registerDevice(SENDER_ID_PUSH, new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {
                Toast.makeText(context, "Device Registered", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, "Device Registered Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
