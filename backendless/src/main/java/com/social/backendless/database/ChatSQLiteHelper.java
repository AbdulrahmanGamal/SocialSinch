package com.social.backendless.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    public static final String TABLE_CONTACTS = "contacts";
    public static final String TABLE_TOTAL_MESSAGES = "total_message";
    public static final String TABLE_MESSAGES = "messages";
    public static final String TABLE_NOTIFICATIONS = "notifications";

    //Columns for Contacts Table
    public static final String COLUMN_ID_CONTACT = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PICTURE = "picture";
    public static final String COLUMN_MODIFIED = "timestamp";

    //Columns for Total Messages table
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TOTAL_MESSAGES = "total_messages";

    //Columns for Messages table
    public static final String COLUMN_ID_MSG = "id";
    public static final String COLUMN_PARTICIPANTS = "participants";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_FROM = "sender";
    public static final String COLUMN_DATE = "timestamp";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_READ = "read";

    //Columns for Notifications table
    public static final String COLUMN_SENDER_ID = "sender_id";
    public static final String COLUMN_MESSAGE_NOTIFICATION = "messages";

    //SQL statement to create table CONTACTS
    private static final String CREATE_TABLE_CONTACTS = "create table "
            + TABLE_CONTACTS + "( " + COLUMN_ID_CONTACT
            + " text primary key, " + COLUMN_NAME
            + " text not null, " + COLUMN_PICTURE
            + " text not null, " + COLUMN_MODIFIED
            + " text not null);";

    //SQL statement to create table TOTAL_MESSAGES
    private static final String CREATE_TABLE_TOTAL_MESSAGES = "create table "
            + TABLE_TOTAL_MESSAGES + "( " + COLUMN_ID
            + " text primary key, " + COLUMN_TOTAL_MESSAGES
            + " integer);";

    //SQL statement to create table MESSAGES
    private static final String CREATE_TABLE_MESSAGES = "create table "
            + TABLE_MESSAGES + "( " + COLUMN_ID_MSG
            + " integer, " + COLUMN_PARTICIPANTS
            + " text not null, " + COLUMN_MESSAGE
            + " text not null, " + COLUMN_FROM
            + " text null, " + COLUMN_DATE
            + " text not null, " + COLUMN_STATUS
            + " integer not null, " + COLUMN_READ
            + " text not null, PRIMARY KEY ( " + COLUMN_ID_MSG + ", " + COLUMN_PARTICIPANTS + " ));";

    //SQL statement to create table NOTIFICATIONS
    private static final String CREATE_TABLE_NOTIFICATIONS = "create table "
            + TABLE_NOTIFICATIONS + "( " + COLUMN_SENDER_ID
            + " text not null, " + COLUMN_MESSAGE_NOTIFICATION
            + " text not null);";

    private static final String TAG = "ChatSQLiteHelper";

    public ChatSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_CONTACTS);
        database.execSQL(CREATE_TABLE_TOTAL_MESSAGES);
        database.execSQL(CREATE_TABLE_MESSAGES);
        database.execSQL(CREATE_TABLE_NOTIFICATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ChatSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOTAL_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
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
