package com.parse.sinch.social;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;

import com.ibm.icu.impl.Assert;
import com.parse.sinch.social.database.ChatBriteDataSource;
import com.parse.sinch.social.model.ChatMessage;
import com.parse.sinch.social.model.Conversation;
import com.squareup.sqlbrite.SqlBrite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by valgood on 3/5/2017.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = "src/main/AndroidManifest.xml",
        packageName = "com.parse.sinch.social")
public class DatabaseTest {
    private Context mMockContext;

    @Before
    public void setUp() {
        mMockContext = ShadowApplication.getInstance().getApplicationContext();
    }

    @After
    public void tearDown() {
        // Empty for now
    }

    @Test
    public void increaseTotalMessage() {
        ChatBriteDataSource dataSource = new ChatBriteDataSource(mMockContext);
        String senderId = "0280CFFE-6C36-D6F2-FFF1-6BF559C87900";
        String recipientId = "8E4B12A7-2B39-778B-FF00-9715DF18DA00";
        String identifier = String.valueOf(senderId + ":" + recipientId);
        for (int i = 0; i < 5; i++) {
            dataSource.increaseTotalMessage(identifier);
            Long result = dataSource.getTotalMessage(identifier);
            Assert.assrt(result > 0);
        }
    }

    @Test
    public void addMessage() {
        ChatBriteDataSource dataSource = new ChatBriteDataSource(mMockContext);
        String senderId = "0280CFFE-6C36-D6F2-FFF1-6BF559C87900";
        String recipientId = "8E4B12A7-2B39-778B-FF00-9715DF18DA00";
        String identifier = String.valueOf(senderId + ":" + recipientId);
        String textBody = "sup yoo";
        String timestamp = "Fri Feb 24 22:57:51 EST 2017";

        dataSource.addNewMessage(senderId, recipientId, textBody, ChatMessage.ChatStatus.SENT, null);

        senderId = "0280CFFE-6C36-D6F2-FFF1-6BF559C87900";
        recipientId = "8E4B12A7-2B39-778B-FF00-9715DF18DA00";
        textBody = "yeah good";

        dataSource.addNewMessage(senderId, recipientId, textBody, ChatMessage.ChatStatus.SENT, null);

        List<ChatMessage> conversations = dataSource.retrieveLastMessages(senderId, recipientId,
                                                                           0, 3);
        Assert.assrt(! conversations.isEmpty() && conversations.size() == 2);

    }
}
