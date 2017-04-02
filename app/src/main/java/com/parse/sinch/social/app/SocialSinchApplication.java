package com.parse.sinch.social.app;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.social.backendless.utils.ApplicationUtils;

public class SocialSinchApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		ApplicationUtils.init(this);
		Fresco.initialize(this);
	}
}
