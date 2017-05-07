package com.social.valgoodchat.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.social.backendless.PublishSubscribeHandler;
import com.social.backendless.bus.RxIncomingEventBus;
import com.social.backendless.data.DataManager;
import com.social.backendless.database.ChatBriteDataSource;
import com.social.backendless.model.EventMessage;
import com.social.backendless.model.EventStatus;
import com.social.backendless.utils.ApplicationUtils;
import com.social.backendless.utils.Constants;
import com.social.backendless.utils.DateUtils;
import com.social.backendless.utils.LoggedUser;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SocialSinchApplication extends Application {

	private static boolean activityVisible;

	@Override
	public void onCreate() {
		super.onCreate();

		ApplicationUtils.init(this);
        PublishSubscribeHandler.initialize(this);
	}

    public static boolean isActivityVisible() {
		return activityVisible;
	}

	public static void activityResumed() {
		activityVisible = true;
		DataManager.updatePresenceInRemote(true);
	}

	public static void activityPaused() {
		activityVisible = false;
        DataManager.updatePresenceInRemote(false);
	}

	public static void clearNotifications(Context context) {
		//Remove Notifications from DB
        ChatBriteDataSource.getInstance(context).deleteNotifications();
	}

//	public static void closeApplicationMessage(Activity activity) {
//		if (activity == null) {
//			return;
//		}
//		new AlertDialog.Builder(activity)
//				.setIcon(android.R.drawable.ic_dialog_alert)
//				.setTitle(R.string.salir)
//				.setMessage(R.string.salir_msj)
//				.setPositiveButton(R.string.aceptar, (dialog, which) -> activity.finish())
//				.setNegativeButton(R.string.cancelar, null)
//				.show();
//	}

    public static void notifyRealTimePresence(EventStatus status) {
        String eventContent = status.toString();
        if (status.equals(EventStatus.OFFLINE)) {
            Date currentTime = Calendar.getInstance(Locale.getDefault()).getTime();
            eventContent = DateUtils.convertDateToString(currentTime);
        }

        EventMessage eventMessage = new EventMessage(LoggedUser.getInstance().getUserIdLogged(),
                Constants.PUBLISH_ALL,
                eventContent,
                status);
        RxIncomingEventBus.getInstance().sendEvent(eventMessage);
    }
}
