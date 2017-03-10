package com.social.sinchservice;

import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.WritableMessage;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import rx.Observable;
import rx.functions.Func1;

public class SinchService extends Service implements SinchClientListener {
		private static final String APP_KEY = "1449b242-7764-4e78-9866-34b50bb52dba";
		private static final String APP_SECRET = "PAlx4YPGrky4iS3eD3Wnag==";
		private static final String ENVIRONMENT = "sandbox.sinch.com";
		private final MessageServiceInterface serviceInterface = new MessageServiceInterface();
		private SinchClient sinchClient = null;
		private MessageClient messageClient = null;
		private String currentUserId;
		
		private Intent broadcastIntent = new Intent("com.parse.sinch.social.TabActivity");
		private LocalBroadcastManager broadcaster;

		private static final String TAG = "SinchService";

		public static final String CURRENT_USER_KEY = "com.social.sinchservice.current.user.key";
		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			broadcaster = LocalBroadcastManager.getInstance(this);
			//get the current user id from Parse
			currentUserId = intent.getStringExtra(CURRENT_USER_KEY);
			if (currentUserId != null && !isSinchClientStarted()) {
				//startSinchClient(currentUserId);
				startSinch2(currentUserId);
			}
			return super.onStartCommand(intent, flags, startId);
		}

		public void startSinch2(String username) {
			final SinchClientHandler sinchClientHandler = new SinchClientHandler(this);
			sinchClientHandler.startSinchClient(this, username).concatMap(new Func1<SinchClient, Observable<String>>() {
				@Override
				public Observable<String> call(SinchClient sinchClient) {
					Log.e(TAG, "adding MessageListener to Message CLIENT!! : ");
					return sinchClientHandler.observableMessageClientListenerWrapper(sinchClient.getMessageClient());
				}
			}).subscribe();
		}
		public void startSinchClient(String username) {
			sinchClient = Sinch.getSinchClientBuilder()
					.context(this)
					.userId(username)
					.applicationKey(APP_KEY)
					.applicationSecret(APP_SECRET)
					.environmentHost(ENVIRONMENT)
					.build();
			//this client listener requires that you define
			//a few methods below
			sinchClient.addSinchClientListener(this);
			sinchClient.setSupportMessaging(true);
			sinchClient.setSupportActiveConnectionInBackground(true);
			sinchClient.checkManifest();
			sinchClient.start();
		}
		
		private boolean isSinchClientStarted() {
			return sinchClient != null && sinchClient.isStarted();
		}
		
		//The next 5 methods are for the sinch client listener
		@Override
		public void onClientFailed(SinchClient client, SinchError error) {
			sinchClient = null;
			
			broadcastIntent.putExtra("success", false);
			broadcaster.sendBroadcast(broadcastIntent);
		}
		
		@Override
		public void onClientStarted(SinchClient client) {
			client.startListeningOnActiveConnection();
			messageClient = client.getMessageClient();
			
			broadcastIntent.putExtra("success", true);
			broadcaster.sendBroadcast(broadcastIntent);
		}
		
		@Override
		public void onClientStopped(SinchClient client) {
			Log.e(TAG, "onClientStopped called");
			sinchClient = null;
		}
		
		@Override
		public void onRegistrationCredentialsRequired(SinchClient client, ClientRegistration clientRegistration) {
			Log.e(TAG, "onRegistrationCredentialsRequired called");
		}
		
		@Override
		public void onLogMessage(int level, String area, String message) {
			Log.d("SinchService", "Level: " + level + " area: " + area + " message: " + message);
		}
		
		@Override
		public IBinder onBind(Intent intent) {
			return serviceInterface;
		}
		
		public void sendMessage(String recipientUserId, String textBody) {
			if (messageClient != null) {
				WritableMessage message = new WritableMessage(recipientUserId, textBody);
				messageClient.send(message);
			}
		}
		
		public void addMessageClientListener(MessageClientListener listener) {
			if (messageClient != null) {
				messageClient.addMessageClientListener(listener);
			}
		}
		
		public void removeMessageClientListener(MessageClientListener listener) {
			if (messageClient != null) {
				messageClient.removeMessageClientListener(listener);
			}
		}
		
		@Override
		public void onDestroy() {
			sinchClient.stopListeningOnActiveConnection();
			sinchClient.terminate();
		}
		
		
		//public interface for ListUsersActivity & MessagingActivity
		public class MessageServiceInterface extends Binder {
			public void sendMessage(String recipientUserId, String textBody) {
				SinchService.this.sendMessage(recipientUserId, textBody);
			}
			public void addMessageClientListener(MessageClientListener listener) {
				SinchService.this.addMessageClientListener(listener);
			}
			public void removeMessageClientListener(MessageClientListener listener) {
				SinchService.this.removeMessageClientListener(listener);
			}
			public boolean isSinchClientStarted() {
				return SinchService.this.isSinchClientStarted();
			}

			public SinchService getService() {
				// Return this instance of LocalService so clients can call public methods.
				return SinchService.this;
			}
		}
}
