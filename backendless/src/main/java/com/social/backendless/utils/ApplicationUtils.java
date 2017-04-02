package com.social.backendless.utils;

import android.content.Context;

import com.backendless.Backendless;

/**
 * Class to handle backendless calls
 */

public class ApplicationUtils {

    public static void init(Context context) {
        Backendless.initApp(context, "67B8DFF8-281D-7293-FF34-E2B84A032F00",
                "91A4FF6A-01C4-C388-FFF1-9389DC345F00", "v1");
    }
}
