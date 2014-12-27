package com.parse.sinch.social;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.parse.ParseUser;
import com.parse.sinch.social.service.SinchService;

public class MessagesActivity extends Activity {

	private String recipientId;
	private String messageBody;
	private SinchService.MessageServiceInterface messageService;
	private String currentUserId;
	private ServiceConnection serviceConnection = new SinchServiceConnection();
	
	private AQuery aq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messaging);

		aq = new AQuery(this);
		
		getActionBar().setTitle(R.string.chat);

		bindService(new Intent(this, SinchService.class), serviceConnection, BIND_AUTO_CREATE);
		//get recipientId from the intent
		Intent intent = getIntent();
		recipientId = intent.getStringExtra("RECIPIENT_ID");
		currentUserId = ParseUser.getCurrentUser().getObjectId();
		//messageBodyField = (EditText) findViewById(R.id.messageBodyField);
		//listen for a click on the send button
		findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//send the message!
				messageBody = aq.id(R.id.messageBodyField).getEditText().getText().toString();
				if (messageBody.isEmpty()) {
					Toast.makeText(getApplicationContext(), "Please enter a message", Toast.LENGTH_LONG).show();
					return;
				}
				messageService.sendMessage(recipientId, messageBody);
				aq.id(R.id.messageBodyField).getEditText().setText("");
			}
		});
	}

	private class SinchServiceConnection implements ServiceConnection {
		@Override
		public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
			messageService = (SinchService.MessageServiceInterface) iBinder;
		}
		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			messageService = null;
		}
	}

	//unbind the service when the activity is destroyed
	@Override
	public void onDestroy() {
		unbindService(serviceConnection);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_llamar_sms, menu);
		return super.onCreateOptionsMenu(menu);
	} 

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_llamar:
			startActivity(new Intent(this, CallingActivity.class));
			return true;
		case R.id.action_sms:
			//mostrarDialogo(R.string.sms);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
