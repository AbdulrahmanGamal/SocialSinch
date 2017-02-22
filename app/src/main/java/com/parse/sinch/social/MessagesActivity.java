package com.parse.sinch.social;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.backendless.Backendless;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.parse.sinch.social.databinding.ActivityChatMainBinding;
import com.parse.sinch.social.utils.Constants;
import com.parse.sinch.social.viewmodel.MessageViewModel;

public class MessagesActivity extends AppCompatActivity {
    private  MessageViewModel messageViewModel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityChatMainBinding activityChatMainBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_chat_main);
        //get recipient information from the intent
        Intent intent = getIntent();
        String recipientId = intent.getStringExtra(Constants.RECIPIENT_ID);
        String currentUserId = Backendless.UserService.loggedInUser();
        //create the view model
        messageViewModel = new MessageViewModel(this, recipientId, currentUserId);
        activityChatMainBinding.setViewModel(messageViewModel);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(intent.getStringExtra(Constants.RECIPIENT_NAME));
        Glide.with(getApplicationContext()).
                load(intent.getStringExtra(Constants.RECIPIENT_AVATAR)).
                into(new ViewTarget<Toolbar, GlideDrawable>(toolbar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation anim) {
                        Toolbar toolbar = this.view;
                        toolbar.setNavigationIcon(resource);
                        // Set your resource on myView and/or start your animation here.
                    }
                });
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
	}

     //unbind the service when the activity is destroyed
	@Override
	public void onDestroy() {
        messageViewModel.removeMessageClientListener();
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
