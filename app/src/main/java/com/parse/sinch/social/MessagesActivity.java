package com.parse.sinch.social;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.bumptech.glide.Glide;
import com.parse.sinch.social.databinding.ActivityChatMainBinding;
import com.parse.sinch.social.model.UserInfo;
import com.parse.sinch.social.utils.Constants;
import com.parse.sinch.social.viewmodel.MessageViewModel;

import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {
    private  MessageViewModel messageViewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityChatMainBinding activityChatMainBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_chat_main);
        //get recipient information from the intent
        Intent intent = getIntent();
        //create the view model
        final UserInfo recipientInfo = new UserInfo();
        recipientInfo.setObjectId(intent.getStringExtra(Constants.RECIPIENT_ID));
        recipientInfo.setFullName(intent.getStringExtra(Constants.RECIPIENT_NAME));
        recipientInfo.setProfilePicture(intent.getStringExtra(Constants.RECIPIENT_AVATAR));
        List<String> recipientsInfo = new ArrayList<>();
        recipientsInfo.add(recipientInfo.getObjectId());
        messageViewModel = new MessageViewModel(this, Backendless.UserService.loggedInUser(),
											    recipientsInfo);
        activityChatMainBinding.setViewModel(messageViewModel);

		getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.background));
		//toolbar settings
        final ImageView profilePic = (ImageView) activityChatMainBinding.
                toolbarChats.findViewById(R.id.conversation_contact_photo);

        Glide.with(this).load(recipientInfo.getProfilePicture()).into(profilePic);
        ((TextView)activityChatMainBinding.toolbarChats.findViewById(R.id.action_bar_title_1)).
                setText(recipientInfo.getFullName());
        ((TextView)activityChatMainBinding.toolbarChats.findViewById(R.id.action_bar_title_2)).
                setText("an hour ago");

        setSupportActionBar(activityChatMainBinding.toolbarChats);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
	}

     //unbind the service when the activity is destroyed
	@Override
	public void onDestroy() {
		super.onDestroy();
		messageViewModel.removeMessageClientListener();
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
