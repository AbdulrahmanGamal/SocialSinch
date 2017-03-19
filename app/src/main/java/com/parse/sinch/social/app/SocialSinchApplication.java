package com.parse.sinch.social.app;

import android.app.Application;

import com.backendless.Backendless;

public class SocialSinchApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		Backendless.initApp(this, "67B8DFF8-281D-7293-FF34-E2B84A032F00",
				"91A4FF6A-01C4-C388-FFF1-9389DC345F00", "v1");
	}
}
