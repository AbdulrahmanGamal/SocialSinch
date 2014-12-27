package com.parse.sinch.social;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.androidquery.AQuery;

public class CallingActivity extends Activity {


	private AQuery aq;
	private Activity activity;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamando);
        
        aq = new AQuery(this);
        
        activity = this;
        
        aq.id(R.id.btnDecline).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				activity.finish();
			}
		});
        
        aq.id(R.id.btnSpeaker).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				
			}
		});
    }
}
