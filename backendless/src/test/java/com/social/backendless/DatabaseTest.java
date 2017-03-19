package com.social.backendless;

import android.content.Context;
import android.os.Build;

import com.example.jorgevalbuena.backendless.BuildConfig;
import com.social.backendless.database.ChatBriteDataSource;
import com.social.backendless.model.ChatMessage;
import com.social.backendless.model.ChatStatus;
import com.social.backendless.utils.DateUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static org.junit.Assert.assertTrue;

/**
 * Unit Test for Database Class
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = "src/main/AndroidManifest.xml",
        packageName = "com.social.sinchservice")
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
        ChatBriteDataSource dataSource = ChatBriteDataSource.getInstance(mMockContext);
        String senderId = "0280CFFE-6C36-D6F2-FFF1-6BF559C87900";
        String recipientId = "8E4B12A7-2B39-778B-FF00-9715DF18DA00";
        String identifier = String.valueOf(senderId + ":" + recipientId);
        for (int i = 0; i < 5; i++) {
            dataSource.increaseTotalMessage(identifier);
            Long result = dataSource.getTotalMessage(identifier);
            assertTrue(result > 0);
        }
    }

    @Test
    public void addMessage() {
        ChatBriteDataSource dataSource = ChatBriteDataSource.getInstance(mMockContext);
        ChatMessage chatMessageSend =
                new ChatMessage("8E4B12A7-2B39-778B-FF00-9715DF18DA00", "sup yoo");
        chatMessageSend.setSenderId("0280CFFE-6C36-D6F2-FFF1-6BF559C87900");
        chatMessageSend.setStatus(ChatStatus.SENT);
        chatMessageSend.setTimestamp(DateUtils.
                convertStringToDate("Fri Feb 24 22:57:51 EST 2017"));
        dataSource.addNewMessage(chatMessageSend);

        ChatMessage chatMessageReceived =
                new ChatMessage("0280CFFE-6C36-D6F2-FFF1-6BF559C87900", "yeah good");
        chatMessageReceived.setSenderId("8E4B12A7-2B39-778B-FF00-9715DF18DA00");
        chatMessageReceived.setStatus(ChatStatus.RECEIVED);
        chatMessageReceived.setTimestamp(DateUtils.
                convertStringToDate("Fri Feb 24 22:59:51 EST 2017"));
        dataSource.addNewMessage(chatMessageReceived);
    }
}

