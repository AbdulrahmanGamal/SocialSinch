package com.parse.sinch.social.app;

import com.parse.Parse;

import android.app.Application;

public class SocialSinchApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		Parse.initialize(this, "jPWRvYFhq9FeOGP9sXcTg0gSJG5Rl51axjTkTalw", "EXjaFOo4PPLC2V4zVH8Df3efwiEslZAIy6rmuyFA");
	}

	
}
