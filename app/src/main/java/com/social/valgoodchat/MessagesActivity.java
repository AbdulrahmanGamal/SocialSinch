package com.social.valgoodchat;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.social.backendless.bus.RxOutgoingEventBus;
import com.social.backendless.data.DataManager;
import com.social.backendless.model.EventMessage;
import com.social.backendless.utils.DateUtils;
import com.social.valgoodchat.custom.TypeWriter;
import com.social.valgoodchat.databinding.ActivityChatMainBinding;
import com.social.valgoodchat.utils.Constants;
import com.social.valgoodchat.utils.Utils;
import com.social.valgoodchat.viewmodel.MessageViewModel;

import io.reactivex.functions.Consumer;

public class MessagesActivity extends AppCompatActivity {
    private static final String TAG = "MessagesActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final ActivityChatMainBinding activityChatMainBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_chat_main);
        //get recipient information from the intent
        Intent intent = getIntent();
        String userId = intent.getStringExtra(Constants.RECIPIENT_ID);
        MessageViewModel messageViewModel = new MessageViewModel(MessagesActivity.this, userId);
        activityChatMainBinding.setViewModel(messageViewModel);

        final ImageView profilePic = (ImageView) activityChatMainBinding.
                toolbarChats.findViewById(R.id.conversation_contact_photo);
        final TextView userNameTextView =
                ((TextView)activityChatMainBinding.toolbarChats.findViewById(R.id.action_bar_title_1));
        final TypeWriter lastTimeSeenTV =
                ((TypeWriter) activityChatMainBinding.toolbarChats.findViewById(R.id.action_bar_title_2));

        getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.background));
        setSupportActionBar(activityChatMainBinding.toolbarChats);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //Get the current user's information from the backend
        DataManager.getUserInformationObservable(userId)
                .subscribe(userInfo -> {
                    //toolbar settings
                    Uri imageUri = Uri.parse(userInfo.getProfilePicture());
                    profilePic.setImageURI(imageUri);
                    userNameTextView.setText(userInfo.getFullName());

                    activityChatMainBinding.toolbarChats.setOnClickListener(v -> {
                        Intent intent1 = new Intent(MessagesActivity.this, ProfileActivity.class);
                        intent1.putExtra(Constants.RECIPIENT_ID, userInfo.getObjectId());
                        intent1.putExtra(Constants.RECIPIENT_AVATAR, userInfo.getProfilePicture());
                        intent1.putExtra(Constants.RECIPIENT_NAME, userInfo.getFullName());
                        intent1.putExtra(Constants.RECIPIENT_LAST_TIME_SEEN, userInfo.getLastSeen());
                        startActivity(intent1);
                    });

                    Utils.formatLastTimeSeenText(MessagesActivity.this,
                                     DateUtils.convertDateToLastSeenFormat(userInfo.getLastSeen()), lastTimeSeenTV);
                    //subscribe to the global event bus to receive events from this user
                    subscribeToLastSeenEvents(lastTimeSeenTV);

                });
	}

    /**
     * Last seen listener to update the last seen text
     * @param lastTimeSeenTV
     */
    private void subscribeToLastSeenEvents(final TextView lastTimeSeenTV) {
        //subscribe to the global event bus to receive events from this user
        RxOutgoingEventBus.getInstance().getMessageObservable().subscribe(new Consumer<EventMessage>() {
            @Override
            public void accept(EventMessage eventMessage) throws Exception {
                Log.e(TAG, "Received this EVENT: " + eventMessage);
                switch (eventMessage.getEventStatus()) {
                    case ONLINE:
                        lastTimeSeenTV.setText(getResources().getString(R.string.status_online));
                        break;
                    case OFFLINE:
                        lastTimeSeenTV.
                                setText(DateUtils.
                                        convertDateToLastSeenFormat(eventMessage.getEventMessage()));
                        break;
                    default:
                        lastTimeSeenTV.setText("");
                        break;
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        DataManager.updateLastSeenFieldInRemote();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DataManager.updateLastSeenFieldInRemote();
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
        case android.R.id.home:
            finish();
            return true;
		case R.id.action_llamar:
			startActivity(new Intent(this, CallingActivity.class));
			return true;
		case R.id.action_sms:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
