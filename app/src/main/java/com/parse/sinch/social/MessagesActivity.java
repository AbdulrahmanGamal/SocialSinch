package com.parse.sinch.social;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.messaging.Message;
import com.backendless.services.messaging.MessageStatus;
import com.bumptech.glide.Glide;
import com.parse.sinch.social.app.SocialSinchApplication;
import com.parse.sinch.social.databinding.ActivityChatMainBinding;
import com.parse.sinch.social.model.UserInfo;
import com.parse.sinch.social.utils.Constants;
import com.parse.sinch.social.viewmodel.MessageViewModel;
import com.social.backendless.PublishSubscribeHandler;
import com.social.backendless.bus.RxOutgoingEventBus;
import com.social.backendless.model.EventMessage;
import com.social.backendless.model.EventStatus;

import java.util.List;

import io.reactivex.functions.Consumer;
import rx.functions.Action1;

public class MessagesActivity extends AppCompatActivity {
    private static final String TAG = "MessagesActivity";

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
        MessageViewModel messageViewModel = new MessageViewModel(this, Backendless.UserService.loggedInUser(),
                recipientInfo.getObjectId());
        activityChatMainBinding.setViewModel(messageViewModel);

		getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.background));
		//toolbar settings
        final ImageView profilePic = (ImageView) activityChatMainBinding.
                toolbarChats.findViewById(R.id.conversation_contact_photo);

        Glide.with(this).load(recipientInfo.getProfilePicture()).into(profilePic);
        ((TextView)activityChatMainBinding.toolbarChats.findViewById(R.id.action_bar_title_1)).
                setText(recipientInfo.getFullName());
        final TextView lastTimeSeenTV = ((TextView)activityChatMainBinding.toolbarChats.findViewById(R.id.action_bar_title_2));

        //subscribe to the global event bus to receive events from this user
        RxOutgoingEventBus.getInstance().getMessageObservable().subscribe(new Consumer<EventMessage>() {
            @Override
            public void accept(EventMessage eventMessage) throws Exception {
                Log.e(TAG, "Received this EVENT: " + eventMessage);
                switch (eventMessage.getEventStatus()) {
                    case ONLINE:
                        lastTimeSeenTV.setText(eventMessage.getEventStatus().toString());
                        break;
                    case OFFLINE:
                        lastTimeSeenTV.setText(eventMessage.getEventMessage());
                        break;
                    default:
                        lastTimeSeenTV.setText("");
                        break;
                }
            }
        });
        setSupportActionBar(activityChatMainBinding.toolbarChats);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
	}

//	/**
//	 * Find if the user we are talking to sent an Online status
//	 * @param eventMessages
//     */
//	private String processEventListReceived(List<Message> eventMessages, String recipientId) {
//		for (Message eventReceived: eventMessages) {
//			if (eventReceived.getHeaders().get(com.social.backendless.utils.Constants.MESSAGE_TYPE_KEY).
//												equals(com.social.backendless.utils.Constants.MESSAGE_TYPE_EVENT_KEY) &&
//				eventReceived.getPublisherId().equals(recipientId)) {
//				return (String) eventReceived.getData();
//			}
//		}
//		return "";
//	}

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
