package com.social.sinchservice;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.social.sinchservice.database.ChatBriteDataSource;
import com.social.sinchservice.model.ChatMessage;
import com.social.sinchservice.model.ChatStatus;
import com.social.sinchservice.utils.DateUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by valgood on 3/5/2017.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private Context mMockContext;

    @Before
    public void setUp() {
        mMockContext = InstrumentationRegistry.getTargetContext();
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
        ChatMessage chatMessageSend = new ChatMessage();
        chatMessageSend.setSenderId("0280CFFE-6C36-D6F2-FFF1-6BF559C87900");
        List<String> recipientIds = new ArrayList<>();
        recipientIds.add("8E4B12A7-2B39-778B-FF00-9715DF18DA00");
        chatMessageSend.setRecipientIds(recipientIds);
        chatMessageSend.setTextBody("sup yoo");
        chatMessageSend.setStatus(ChatStatus.SENT);
        chatMessageSend.setTimestamp(DateUtils.convertStringToDate("Fri Feb 24 22:57:51 EST 2017"));
        dataSource.addNewMessage(chatMessageSend);

        ChatMessage chatMessageReceived = new ChatMessage();
        chatMessageReceived.setSenderId("8E4B12A7-2B39-778B-FF00-9715DF18DA00");
        List<String> recipientIdsReceived = new ArrayList<>();
        recipientIdsReceived.add("0280CFFE-6C36-D6F2-FFF1-6BF559C87900");
        chatMessageReceived.setRecipientIds(recipientIdsReceived);
        chatMessageReceived.setTextBody("yeah good");
        chatMessageReceived.setStatus(ChatStatus.RECEIVED);
        chatMessageReceived.setTimestamp(DateUtils.convertStringToDate("Fri Feb 24 22:59:51 EST 2017"));
        dataSource.addNewMessage(chatMessageReceived);
    }
}
