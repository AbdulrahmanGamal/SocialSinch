package com.social.valgoodchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.social.backendless.model.UserInfo;
import com.social.valgoodchat.app.SocialSinchApplication;
import com.social.valgoodchat.custom.ProfileHeaderView;
import com.social.valgoodchat.utils.Constants;
import com.social.valgoodchat.utils.ImageLoading;

/**
 * Created by valgood on 3/25/2017.
 */

public class ProfileActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.7f;

    private boolean mIsTheTitleVisible = false;

    private AppBarLayout appbar;
    private ImageView coverImage;
    private Toolbar toolbar;
    private ImageView avatar;

    private void findViews() {
        appbar = (AppBarLayout)findViewById( R.id.chat_bar_layout );
        coverImage = (ImageView)findViewById( R.id.profile_placeholder );
        toolbar = (Toolbar)findViewById( R.id.toolbar );
        avatar = (ImageView) findViewById(R.id.avatar);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        findViews();

        toolbar.setTitle("");
        appbar.addOnOffsetChangedListener(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);

        }

        //get recipient information from the intent
        Intent intent = getIntent();
        final UserInfo recipientInfo = new UserInfo();
        recipientInfo.setObjectId(intent.getStringExtra(Constants.RECIPIENT_ID));
        recipientInfo.setFullName(intent.getStringExtra(Constants.RECIPIENT_NAME));
        recipientInfo.setProfilePicture(intent.getStringExtra(Constants.RECIPIENT_AVATAR));
        recipientInfo.setLastSeen(intent.getStringExtra(Constants.RECIPIENT_LAST_TIME_SEEN));

        ProfileHeaderView headerView = (ProfileHeaderView) findViewById(R.id.float_header_view);
        headerView.bindTo(recipientInfo.getFullName(),
                          recipientInfo.getLastSeen());
        //set avatar and cover
        ImageLoading.loadContactPicture(recipientInfo.getProfilePicture(), avatar);
        coverImage.setImageResource(R.drawable.profile_background);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.ColorPrimary));
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
               toolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
                mIsTheTitleVisible = false;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SocialSinchApplication.activityPaused();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SocialSinchApplication.activityResumed();
    }
}
