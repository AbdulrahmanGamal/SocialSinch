package com.parse.sinch.social.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.parse.sinch.social.model.ChatMessage;
import com.parse.sinch.social.model.Conversation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Allows to connect to the local DB to retrieve and save messages.
 *
 */
public class ChatDataSource {
    // Database fields
    private SQLiteDatabase mChatDatabase;
    private ChatSQLiteHelper mChatDbHelper;

    private static final String TAG = "ChatDataSource";

    public ChatDataSource(Context context) {
        mChatDbHelper = new ChatSQLiteHelper(context);
    }

    public void open() throws SQLException {
        mChatDatabase = mChatDbHelper.getWritableDatabase();
    }

    public void close() {
        mChatDbHelper.close();
    }

    private Callable<List<Conversation>> queryTotalMessages() {
        return new Callable<List<Conversation>>() {
            @Override
            public List<Conversation> call() {
                open();
                List<Conversation> listConversations = new ArrayList<>();
                Cursor cursor = mChatDatabase.rawQuery("SELECT * FROM " + ChatSQLiteHelper.TABLE_TOTAL_MESSAGES, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        listConversations.add(new Conversation(cursor.getString(0), cursor.getInt(1)));
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
                close();
                return listConversations;
            }
        };
    }

    private Callable<List<ChatMessage>> queryLastMessages(final String user1, final String user2,
                                                          final int starting, final int ending) {
        return new Callable<List<ChatMessage>>() {
            @Override
            public List<ChatMessage> call() {
                open();
                StringBuilder query = new StringBuilder("SELECT * FROM " +
                        ChatSQLiteHelper.TABLE_MESSAGES + " WHERE "
                        + ChatSQLiteHelper.COLUMN_ID_MSG + " IN ( "
                );
                //append the range of messages
                for (int index = starting; index < ending; index++) {
                    query.append("'").append(user1).append(":").
                            append(user2).append(":").append(String.valueOf(index)).append("' , ");
                }
                //remove last comma
                query.deleteCharAt(query.lastIndexOf(","));
                query.append(" )");
                List<ChatMessage> listConversations = new ArrayList<>();
                Cursor cursor = mChatDatabase.rawQuery(query.toString(), null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        String identifier = cursor.getString(0);
                        String idSplit[] = identifier.split(":");
                        String fromUser = cursor.getString(2);
                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.setSenderId(fromUser);
                        List<String> recipientIds = new ArrayList<>();
                        if (fromUser.equals(user1)) {
                            recipientIds.add(user2);
                        }
                        chatMessage.setRecipientIds(recipientIds);
                        chatMessage.setMessageId(Long.valueOf(idSplit[2]));
                        chatMessage.setTextBody(cursor.getString(1));
                        listConversations.add(chatMessage);
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
                close();
                return listConversations;
            }
        };
    }

    private Callable<Long> querySaveMessage(final String senderId,
                                     final String recipientId,
                                     final long totalMessages,
                                     final String messageText,
                                     final boolean wasReceived) {

        return new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                open();
                StringBuilder identifier;
                if (wasReceived) {
                    identifier = new StringBuilder(recipientId).append(":").append(senderId);
                } else {
                    identifier = new StringBuilder(senderId).append(":").append(recipientId);
                }
                identifier.append(":").append(String.valueOf(totalMessages));
                mChatDatabase.execSQL(
                        "INSERT INTO " + ChatSQLiteHelper.TABLE_MESSAGES +
                                " ( " + ChatSQLiteHelper.COLUMN_ID_MSG + ", " +
                                ChatSQLiteHelper.COLUMN_MESSAGE + ", " +
                                ChatSQLiteHelper.COLUMN_FROM
                                + " ) VALUES ( ?, ?, ? ) ",
                        new String[]{identifier.toString(), messageText, senderId});
                close();
                return totalMessages;
            }
        };
    }
    private Callable<Long> addTotalMessage(final String senderId,
                                           final String recipientId,
                                           final boolean wasReceived) {
        return new Callable<Long>() {
            @Override
            public Long call() {
                Long totalMessages = 0L;
                StringBuilder identifier;
                if (wasReceived) {
                    identifier = new StringBuilder(recipientId).append(":").append(senderId);
                } else {
                    identifier = new StringBuilder(senderId).append(":").append(recipientId);
                }
                mChatDatabase.execSQL(
                        "INSERT " +
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
                        "    )", new String[]{identifier.toString(),
                                              identifier.toString(),
                                              identifier.toString()});

                StringBuilder queryTotalMessages = new StringBuilder("SELECT " +
                        ChatSQLiteHelper.COLUMN_TOTAL_MESSAGES + " FROM ").
                        append(ChatSQLiteHelper.TABLE_TOTAL_MESSAGES).append(" WHERE ").
                        append(ChatSQLiteHelper.COLUMN_ID).append(" LIKE ?");
                Cursor cursor = mChatDatabase.rawQuery(queryTotalMessages.toString(),
                                       new String[]{identifier.toString()});
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    totalMessages = cursor.getLong(0);
                    cursor.close();
                }
                return totalMessages;
            }
        };
    }

    public Observable<Long> addChatMessage(final String senderId,
                                         final String recipientId,
                                         final String messageText,
                                         final boolean wasReceived) {

      return addMessageTotal(senderId, recipientId, wasReceived).flatMap(new Func1<Long, Observable<Long>>() {
            @Override
            public Observable<Long> call(Long totalMessages) {
                return saveMessage(senderId, recipientId, totalMessages, messageText, wasReceived);
            }
        });
    }

//    public Observable<List<Conversation>> getTotalMessages() {
//        return ChatSQLiteHelper.makeObservable(queryTotalMessages())
//                .subscribeOn(Schedulers.computation());
//    }
//
    public Observable<List<ChatMessage>> getLastMessages(final String user1, final String user2,
                                                          final int starting, final int ending) {
        return ChatSQLiteHelper.makeObservable(queryLastMessages(user1, user2, starting, ending))
                .subscribeOn(Schedulers.computation());
    }

    private Observable<Long> saveMessage(String senderId, String recipientId,
                                         Long totalMessages, String message,
                                         boolean wasReceived) {
        return ChatSQLiteHelper.makeObservable(querySaveMessage(senderId, recipientId,
                                                                totalMessages, message, wasReceived))
                .subscribeOn(Schedulers.computation()).doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        open();
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        close();
                    }
                }); // note: do not use Schedulers.io()
    }

    private Observable<Long> addMessageTotal(String senderId, String recipientId, boolean wasRecived) {
        return ChatSQLiteHelper.makeObservable(addTotalMessage(senderId, recipientId, wasRecived))
                .subscribeOn(Schedulers.computation()); // note: do not use Schedulers.io()
    }
}
