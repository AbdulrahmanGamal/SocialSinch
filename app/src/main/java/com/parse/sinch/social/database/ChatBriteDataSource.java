package com.parse.sinch.social.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.parse.sinch.social.model.ChatMessage;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import rx.schedulers.Schedulers;

/**
 * Created by valgood on 3/5/2017.
 */

public class ChatBriteDataSource {
    private ChatSQLiteHelper mChatDbHelper;
    private BriteDatabase mChatBriteDB;

    private static final String TAG = "ChatBriteDataSource";

    public ChatBriteDataSource(Context context) {
        mChatDbHelper = new ChatSQLiteHelper(context);
        //create and configure sqlbrite
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        mChatBriteDB = sqlBrite.wrapDatabaseHelper(mChatDbHelper, Schedulers.io());
        mChatBriteDB.setLoggingEnabled(true);
    }

    /**
     * Increase in 1 the total messages exchanged between senderId and recipientId
     * @param identifier (senderId:recipientId)
     * @return
     */
    public void increaseTotalMessage(final String identifier) throws SQLException {
//        if (wasReceived) {
//            identifier = new StringBuilder(recipientId).append(":").append(senderId);
//        } else {
//
//        }
//
        String query = "INSERT " +
                "    OR REPLACE " +
                "INTO " +
                "    " + ChatSQLiteHelper.TABLE_TOTAL_MESSAGES + " ( " +
                ChatSQLiteHelper.COLUMN_ID + ", " +
                ChatSQLiteHelper.COLUMN_TOTAL_MESSAGES + " ) " +
                "VALUES " +
                "    (?, " +
                "        (SELECT " +
                "           CASE  " +
                "              WHEN exists(SELECT 1  FROM " +
                ChatSQLiteHelper.TABLE_TOTAL_MESSAGES +
                " WHERE " + ChatSQLiteHelper.COLUMN_ID +
                " LIKE ?) " +
                "              THEN (SELECT " + ChatSQLiteHelper.COLUMN_TOTAL_MESSAGES +
                " + 1 FROM " + ChatSQLiteHelper.TABLE_TOTAL_MESSAGES +
                " WHERE " + ChatSQLiteHelper.COLUMN_ID + " LIKE ?) " +
                "              ELSE 1 " +
                "           END " +
                "         ) " +
                "    )";
        mChatBriteDB.execute(query, identifier, identifier, identifier);
    }

    /**
     * Obtains the total messages exchanged between senderId and recipientId
     * @param identifier(senderId:recipientId)
     * @return
     * @throws SQLException
     */
    public Long getTotalMessage(final String identifier) throws SQLException {
        Long result = -1L;
        String queryTotalMessages = String.valueOf("SELECT " +
                ChatSQLiteHelper.COLUMN_TOTAL_MESSAGES + " FROM " +
                ChatSQLiteHelper.TABLE_TOTAL_MESSAGES) + " WHERE " +
                ChatSQLiteHelper.COLUMN_ID + " LIKE ?";
        Cursor cursor = mChatBriteDB.query(queryTotalMessages, identifier);
        if (cursor != null) {
            cursor.moveToFirst();
            result = cursor.getLong(0);
        }
        return result;
    }

    /**
     * Verify is the message is already in DB
     * @param senderId
     * @param recipientId
     * @param textBody
     * @return
     * @throws SQLException
     */
    public Long verifyMessage(final String senderId,
                              final String recipientId,
                              final String textBody) throws SQLException {
        Long messageId = -1L;
        String identifier = String.valueOf(senderId + ":" + recipientId);
        String queryMessages = String.valueOf("SELECT " +
                ChatSQLiteHelper.COLUMN_ID_MSG + " FROM " +
                ChatSQLiteHelper.TABLE_MESSAGES) + " WHERE " +
                ChatSQLiteHelper.COLUMN_PARTICIPANTS + " = ? AND " +
                ChatSQLiteHelper.COLUMN_MESSAGE + " = ? ";
        Cursor cursor = mChatBriteDB.query(queryMessages, identifier, textBody);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            messageId = cursor.getLong(0);
        }
        return messageId;
    }

    /**
     * find a message wich status is sent to match the delivery message later
     * @param sentId
     * @return
     * @throws SQLException
     */
    public Long verifySentMessage(final String sentId) throws SQLException {
        Long messageId = -1L;
        String querySentMessage = String.valueOf("SELECT " +
                ChatSQLiteHelper.COLUMN_ID_MSG + " FROM " +
                ChatSQLiteHelper.TABLE_MESSAGES) + " WHERE " +
                ChatSQLiteHelper.COLUMN_SENT_ID + " = ? ";
        Cursor cursor = mChatBriteDB.query(querySentMessage, sentId);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            messageId = cursor.getLong(0);
        }
        return messageId;
    }
    /**
     * Changes the status for a message
     * @param messageId
     * @param status
     * @throws SQLException
     */
    public void updateMessageStatus(Long messageId, ChatMessage.ChatStatus status) throws SQLException {
        String queryUpdateMessage = String.valueOf("UPDATE " +
                ChatSQLiteHelper.TABLE_MESSAGES) + " SET " +
                ChatSQLiteHelper.COLUMN_STATUS + " = ?  WHERE " +
                ChatSQLiteHelper.COLUMN_ID_MSG + " = ? ";
        mChatBriteDB.execute(queryUpdateMessage, status.name(), String.valueOf(messageId));
    }

    public Long addNewMessage(final String senderId,
                              final String recipientId,
                              final String textBody,
                              final ChatMessage.ChatStatus status,
                              final String sendId) throws SQLException {
        //first increase total message between the users
        String identifier;
        if (status.equals(ChatMessage.ChatStatus.RECEIVED)) {
            identifier = String.valueOf(recipientId + ":" + senderId);
        } else {
            identifier = String.valueOf(senderId + ":" + recipientId);
        }
        increaseTotalMessage(identifier);
        //get the new total message
        Long totalMessages = getTotalMessage(identifier);
        mChatBriteDB.execute(
                "INSERT INTO " + ChatSQLiteHelper.TABLE_MESSAGES +
                        " ( " + ChatSQLiteHelper.COLUMN_ID_MSG + ", " +
                        ChatSQLiteHelper.COLUMN_PARTICIPANTS + ", " +
                        ChatSQLiteHelper.COLUMN_MESSAGE + ", " +
                        ChatSQLiteHelper.COLUMN_FROM + ", " +
                        ChatSQLiteHelper.COLUMN_SENT_ID + ", " +
                        ChatSQLiteHelper.COLUMN_STATUS
                        + " ) VALUES ( ?, ?, ? , ? , ?, ? ) ", totalMessages, identifier,
                                                           textBody, senderId, sendId, status);
        return totalMessages;
    }

    public List<ChatMessage> retrieveLastMessages(final String user1, final String user2,
                                                   final int starting, final int ending) throws SQLException{
        List<ChatMessage> listConversations = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM " +
                ChatSQLiteHelper.TABLE_MESSAGES + " WHERE " +
                ChatSQLiteHelper.COLUMN_FROM + " = ? OR " + ChatSQLiteHelper.COLUMN_FROM +
                " = ? ORDER BY " + ChatSQLiteHelper.COLUMN_ID_MSG + " ASC "
//                + ChatSQLiteHelper.COLUMN_ID_MSG + " IN ( "
        );
//        //append the range of messages
//        for (int index = starting; index < ending; index++) {
//            query.append("'").append(user1).append(":").
//                    append(user2).append(":").append(String.valueOf(index)).append("' , ");
//        }
//        //remove last comma
//        query.deleteCharAt(query.lastIndexOf(","));
//        query.append(" )");
        Cursor cursor = mChatBriteDB.query(query.toString(), user1, user2);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Long id = cursor.getLong(0);
                String fromUser = cursor.getString(3);
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMessageId(id);
                chatMessage.setSenderId(fromUser);
                List<String> recipientIds = new ArrayList<>();
                if (fromUser.equals(user1)) {
                    recipientIds.add(user2);
                } else {
                    recipientIds.add(user1);
                }
                chatMessage.setRecipientIds(recipientIds);
                chatMessage.setTextBody(cursor.getString(2));
                listConversations.add(chatMessage);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return listConversations;
    }
}
