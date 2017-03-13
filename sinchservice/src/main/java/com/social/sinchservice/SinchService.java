package com.social.sinchservice;

import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.WritableMessage;
import com.social.sinchservice.model.ChatMessage;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class SinchService extends Service {
		private final MessageServiceInterface serviceInterface = new MessageServiceInterface();
		private SinchClientHandler mSinchClientHandler;
		private String currentUserId;

		private static final String TAG = "SinchService";

		public static final String CURRENT_USER_KEY = "com.social.sinchservice.current.user.key";
		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			//get the current user id from Parse
			currentUserId = intent.getStringExtra(CURRENT_USER_KEY);
            mSinchClientHandler = new SinchClientHandler();
			if (currentUserId != null && !mSinchClientHandler.isSinchClientStarted()) {
				mSinchClientHandler.startSinchClient(this, currentUserId).subscribe();
                SinchServiceConnection.getInstance(this, currentUserId);
			}
			return super.onStartCommand(intent, flags, startId);
		}
		private boolean isSinchClientStarted() {
			return mSinchClientHandler.isSinchClientStarted();
		}
		
		@Override
		public IBinder onBind(Intent intent) {
			return serviceInterface;
		}
		
		private void sendMessage(String recipientUserId, String textBody) {
			mSinchClientHandler.sendMessage(recipientUserId, textBody);
		}

        private List<ChatMessage> retrieveLastMessages(String senderId, String recipientId, int max) {
            return mSinchClientHandler.retrieveLastMessages(senderId, recipientId, max);
        }

		@Override
		public void onDestroy() {
            mSinchClientHandler.terminate();
		}
		
		
		//public interface for Activities to allow comunication with the service
		public class MessageServiceInterface extends Binder {
			public void sendMessage(String recipientUserId, String textBody) {
				SinchService.this.sendMessage(recipientUserId, textBody);
			}
			public boolean isSinchClientStarted() {
				return SinchService.this.isSinchClientStarted();
			}

            public List<ChatMessage> retrieveLastMessages(String senderId,
                                                          String recipientId,
                                                          int max) {
               return SinchService.this.retrieveLastMessages(senderId, recipientId, max);
            }
        }
}
