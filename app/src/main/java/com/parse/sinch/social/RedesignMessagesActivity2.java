package com.parse.sinch.social;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.parse.sinch.social.custom.ProfileHeaderView;
import com.social.backendless.model.UserInfo;
import com.parse.sinch.social.utils.Constants;

/**
 * Created by valgood on 3/25/2017.
 */

public class RedesignMessagesActivity2 extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.7f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsing;
    private ImageView coverImage;
    private Toolbar toolbar;
    private SimpleDraweeView avatar;

    private void findViews() {
        appbar = (AppBarLayout)findViewById( R.id.chat_bar_layout );
        collapsing = (CollapsingToolbarLayout)findViewById( R.id.chat_collapsing_toolbar );
        coverImage = (ImageView)findViewById( R.id.profile_placeholder );
        toolbar = (Toolbar)findViewById( R.id.toolbar );
        avatar = (SimpleDraweeView) findViewById(R.id.avatar);
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
        //startAlphaAnimation(textviewTitle, 0, View.INVISIBLE);
        //startAlphaAnimation(textviewSubTitle, 0, View.INVISIBLE);

        //get recipient information from the intent
        Intent intent = getIntent();
        final UserInfo recipientInfo = new UserInfo();
        recipientInfo.setObjectId(intent.getStringExtra(Constants.RECIPIENT_ID));
        recipientInfo.setFullName(intent.getStringExtra(Constants.RECIPIENT_NAME));
        recipientInfo.setProfilePicture(intent.getStringExtra(Constants.RECIPIENT_AVATAR));
        recipientInfo.setLastSeen(intent.getStringExtra(Constants.RECIPIENT_LAST_TIME_SEEN));

        //((TextView)findViewById(R.id.textview_title)).setText(recipientInfo.getFullName());
        //((TextView)findViewById(R.id.toolbar_user_last_seen)).setText(recipientInfo.getLastSeen());

//        ((TextView)findViewById(R.id.chat_header_username_text)).
//                setText(recipientInfo.getFullName());

        //last time seen text
        //TypeWriter lastTimeSeenTV = ((TypeWriter) findViewById(R.id.user_last_seen));
        //formatLastTimeSeenText(recipientInfo.getLastSeen(), lastTimeSeenTV);

        ProfileHeaderView headerView = (ProfileHeaderView) findViewById(R.id.float_header_view);
        headerView.bindTo(recipientInfo.getFullName(),
                          recipientInfo.getLastSeen());
        //set avatar and cover
        Uri imageUri = Uri.parse(recipientInfo.getProfilePicture());
        avatar.setImageURI(imageUri);
        coverImage.setImageResource(R.drawable.profile_background);

//        this.mChatMessageAdapter = new ChatMessageAdapter(this, recipientInfo.getObjectId(), this);
//
//        mChatRecyclerView = (RecyclerView) findViewById(R.id.chat_list_view);
//        mChatRecyclerView.setAdapter(mChatMessageAdapter);
//        mChatRecyclerView.setLayoutManager(createLayoutManager());
//        mChatRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v,
//                                       int left, int top, int right, int bottom,
//                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                if (bottom < oldBottom) {
//                    onItemInserted();
//                }
//            }
//        });
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
                //startAlphaAnimation(textviewTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                //startAlphaAnimation(textviewSubTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.ColorPrimary));
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                //startAlphaAnimation(textviewTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                //startAlphaAnimation(textviewSubTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
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
}
