package com.social.valgoodchat.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.backendless.push.BackendlessPushService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.social.backendless.PublishSubscribeHandler;
import com.social.backendless.bus.RxOutgoingMessageBus;
import com.social.backendless.database.ChatBriteDataSource;
import com.social.backendless.model.ChatMessage;
import com.social.backendless.model.UserInfo;
import com.social.backendless.utils.Constants;
import com.social.backendless.utils.DateUtils;
import com.social.backendless.utils.LoggedUser;
import com.social.valgoodchat.MessagesActivity;
import com.social.valgoodchat.R;
import com.social.valgoodchat.app.SocialSinchApplication;
import com.social.valgoodchat.utils.ImageLoading;

import java.util.concurrent.ExecutionException;

import io.reactivex.disposables.Disposable;

/**
 * Service to process the push notification messages
 */
public class SocialPushService extends BackendlessPushService {
    private static final String TAG = "SocialPushService";
    private Disposable mDisposable;
    private Handler mHandler;
    private PublishSubscribeHandler mSubscriberHandler;


    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
    }

    @Override
    public void onRegistered(Context context, String registrationId) {
        Log.d(TAG, "Device registered " + registrationId);
        initSubscriber(context);
    }

    private void initSubscriber(final Context context) {
        mDisposable = RxOutgoingMessageBus.getInstance().getMessageObservable().subscribe(chatMessage -> {
            //if there's no activity visible, show the notification
            if (!SocialSinchApplication.isActivityVisible() &&
                LoggedUser.getInstance().getUserIdLogged().equals(chatMessage.getRecipientId())) {
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                UserInfo senderInfo = ChatBriteDataSource.getInstance(context).getContactById(chatMessage.getSenderId());
                mNotificationManager.notify(NOTIFICATION_ID, createNotification(context, true, senderInfo, chatMessage));
            }
        });
        mSubscriberHandler = new PublishSubscribeHandler(context);
    }
    @Override
    public void onUnregistered( Context context, Boolean unregistered ) {
        Log.d(TAG, "Device unregistered ");
        if (unregistered) {
            mDisposable.dispose();
            mSubscriberHandler.dispose();
        }
    }

    @Override
    public boolean onMessage( Context context, Intent intent ) {
        String from = intent.getStringExtra(Constants.PUBLISHER_KEY);
        if (from != null && from.equals(LoggedUser.getInstance().getUserIdLogged())) {
            //don't care about my own event messages
            return false;
        }

        String recipient = intent.getStringExtra(Constants.RECIPIENT_KEY);
        if (recipient != null &&
            (recipient.equals(LoggedUser.getInstance().getUserIdLogged()) ||
                    recipient.equals(Constants.PUBLISH_ALL))) {
            String messageType = intent.getStringExtra(Constants.MESSAGE_TYPE_KEY);
            String message = intent.getStringExtra(Constants.MESSAGE_KEY);
            if (mSubscriberHandler == null) {
                initSubscriber(context);
            }
            mSubscriberHandler.processIncomingMessage(messageType, message);
        }
        // When returning 'true', default Backendless onMessage implementation will be executed.
        // The default implementation displays the notification in the Android Notification Center.
        // Returning false, cancels the execution of the default implementation.
        return false;
    }

    @Override
    public void onError( Context context, String message ) {
        Log.e(TAG, "Error registering device: " + message);
    }

   private Notification createNotification(Context context,
                                           boolean makeHeadsUpNotification,
                                           UserInfo senderInfo,
                                           ChatMessage messageReceived) {

       final RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.new_message_notification);
       try {
           Bitmap bitmap = Glide.with(context.getApplicationContext()) // safer!
                   .load(senderInfo.getProfilePicture())
                   .asBitmap().into(-1,-1).get();
           if(!bitmap.isRecycled()) {
               Bitmap roundedBitmap = ImageLoading.getCircleBitmap(bitmap);
               rv.setImageViewBitmap(R.id.notification_icon, roundedBitmap);
           }
       } catch (InterruptedException | ExecutionException e) {
           e.printStackTrace();
           rv.setImageViewResource(R.id.notification_icon, R.drawable.ic_launcher);
       }

       rv.setTextViewText(R.id.notification_headline, senderInfo.getFullName());
       rv.setTextViewText(R.id.notification_message, messageReceived.getTextBody());
       rv.setTextViewText(R.id.notification_date, DateUtils.convertChatDate(messageReceived.getTimestamp()));

       NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentTitle(senderInfo.getFullName())
                .setContentText(messageReceived.getTextBody())
                .setContent(rv);

        notificationBuilder.setCategory(Notification.CATEGORY_MESSAGE);
        if (makeHeadsUpNotification) {
            Intent push = new Intent();
            push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            push.setClass(this, MessagesActivity.class);

            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                    push, PendingIntent.FLAG_CANCEL_CURRENT);
            notificationBuilder
                    .setContentText(messageReceived.getTextBody())
                    .setFullScreenIntent(fullScreenPendingIntent, true);
        }

        Notification notification = notificationBuilder.build();

       NotificationTarget notificationTarget = new NotificationTarget(context, rv,
               R.id.notification_icon,
               notification,
               NOTIFICATION_ID);


       mHandler.post(() -> {
//           Glide.with(context.getApplicationContext()) // safer!
//                   .load(senderInfo.getProfilePicture())
//                   .asBitmap().into(new SimpleTarget<Bitmap>(100,100) {
//               @Override
//               public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
//                   Bitmap roundedBitmap = ImageLoading.getCircleBitmap(resource);
//                   rv.setImageViewBitmap(R.id.notification_icon, roundedBitmap);
//                   //Glide.with(context.getApplicationContext()).load(roundedBitmap).asBitmap().into(notificationTarget);
//               }
//           });
       });
       return notification;
   }
}
