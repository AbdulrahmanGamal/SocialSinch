package com.social.valgoodchat.push;

import com.backendless.push.BackendlessBroadcastReceiver;

/**
 * Receiver to obtain the push notification service
 */

public class SocialPushReceiver extends BackendlessBroadcastReceiver {
    @Override
    public Class<SocialPushService> getServiceClass() {
        return SocialPushService.class;
    }
}
