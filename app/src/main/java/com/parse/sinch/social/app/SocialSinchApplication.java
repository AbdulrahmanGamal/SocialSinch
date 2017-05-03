package com.parse.sinch.social.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.parse.sinch.social.R;
import com.social.backendless.utils.ApplicationUtils;

public class SocialSinchApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		ApplicationUtils.init(this);
		Fresco.initialize(this);
	}

	public static void closeApplicationMessage(Activity activity) {
		if (activity == null) {
			return;
		}
		new AlertDialog.Builder(activity)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.salir)
				.setMessage(R.string.salir_msj)
				.setPositiveButton(R.string.aceptar, (dialog, which) -> activity.finish())
				.setNegativeButton(R.string.cancelar, null)
				.show();
	}
}
