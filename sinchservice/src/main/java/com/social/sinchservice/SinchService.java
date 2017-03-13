package com.social.sinchservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


public class SinchService extends Service {
    private final MessageServiceInterface serviceInterface = new MessageServiceInterface();
    private SinchClientHandler mSinchClientHandler;
    public static final String CURRENT_USER_KEY = "com.social.sinchservice.current.user.key";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //get the logged user id from intent
        String currentUserId = intent.getStringExtra(CURRENT_USER_KEY);
        mSinchClientHandler = new SinchClientHandler(getApplicationContext());
        if (currentUserId != null && !mSinchClientHandler.isSinchClientStarted()) {
            mSinchClientHandler.startSinchClient(this, currentUserId).subscribe();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceInterface;
    }

    @Override
    public void onDestroy() {
        mSinchClientHandler.terminate();
    }


    /**
     * Checks the state of the sinch client
     * @return
     */
    private boolean isSinchClientStarted() {
        return mSinchClientHandler.isSinchClientStarted();
    }

    /**
     * Send a instant message through the sinch client
     * @param recipientUserId
     * @param textBody
     */
    private void sendMessage(String recipientUserId, String textBody) {
        mSinchClientHandler.sendMessage(recipientUserId, textBody);
    }

//    private List<ChatMessage> retrieveLastMessages(String senderId, String recipientId, int max) {
//        return mSinchClientHandler.retrieveLastMessages(senderId, recipientId, max);
//    }


    /**
     * Class used to bind with the service
     */
    public class MessageServiceInterface extends Binder {
        public void sendMessage(String recipientUserId, String textBody) {
            SinchService.this.sendMessage(recipientUserId, textBody);
        }

        public boolean isSinchClientStarted() {
            return SinchService.this.isSinchClientStarted();
        }

//        public List<ChatMessage> retrieveLastMessages(String senderId,
//                                                      String recipientId,
//                                                      int max) {
//            return SinchService.this.retrieveLastMessages(senderId, recipientId, max);
//        }
    }
}
