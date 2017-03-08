package com.social.sinchservice.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.concurrent.Callable;

/**
 * Class to handle the internal DB where the messages are stored
 *
 * The Database Structure was taken from
 * http://qnimate.com/database-design-for-storing-chat-messages/
 *
 *
 */
public class ChatSQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "chat.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TOTAL_MESSAGES = "total_message";
    public static final String TABLE_MESSAGES = "messages";

    //Columns for Total Messages table
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TOTAL_MESSAGES = "total_messages";

    //Columns for Messages table
    public static final String COLUMN_ID_MSG = "id";
    public static final String COLUMN_PARTICIPANTS = "participants";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_FROM = "sender";
    public static final String COLUMN_SENT_ID = "sentId";
    public static final String COLUMN_DATE = "timestamp";
    public static final String COLUMN_STATUS = "status";

    //SQL statement to create table TOTAL_MESSAGES
    private static final String CREATE_TABLE_TOTAL_MESSAGES = "create table "
            + TABLE_TOTAL_MESSAGES + "( " + COLUMN_ID
            + " text primary key, " + COLUMN_TOTAL_MESSAGES
            + " integer);";

    //SQL statement to create table MESSAGES
    private static final String CREATE_TABLE_MESSAGES = "create table "
            + TABLE_MESSAGES + "( " + COLUMN_ID_MSG
            + " integer primary key, " + COLUMN_PARTICIPANTS
            + " text not null, " + COLUMN_MESSAGE
            + " text not null, " + COLUMN_FROM
            + " text not null, " + COLUMN_SENT_ID
            + " text null, " + COLUMN_DATE
            + " text not null, " + COLUMN_STATUS
            + " text not null );";

    private static final String TAG = "ChatSQLiteHelper";

    public ChatSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_TOTAL_MESSAGES);
        database.execSQL(CREATE_TABLE_MESSAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ChatSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOTAL_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        onCreate(db);
    }

//    public static <T> Observable<T> makeObservable(final Callable<T> func) {
//        return Observable.create(
//                new Observable.OnSubscribe<T>() {
//                    @Override
//                    public void call(Subscriber<? super T> subscriber) {
//                        try {
//                            subscriber.onNext(func.call());
//                        } catch(Exception ex) {
//                            Log.e(TAG, "Error reading from the database", ex);
//                        }
//                    }
//                });
//    }
}
