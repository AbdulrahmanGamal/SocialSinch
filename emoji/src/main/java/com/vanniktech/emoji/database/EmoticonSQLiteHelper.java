package com.vanniktech.emoji.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Class to handle the internal DB where the animated emoticons URLs are stored
 *
 */
public class EmoticonSQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "valmoticon.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_EMOTICON = "emoticon";

    //Columns for Emoticon Table
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE_ADDED = "timestamp";

    //SQL statement to create table EMOTICON
    private static final String CREATE_TABLE_EMOTICON = "create table "
            + TABLE_EMOTICON + "( " + COLUMN_NAME
            + " text primary key, " + COLUMN_DATE_ADDED
            + " text );";

    private static final String TAG = "EmoticonSQLiteHelper";

    public EmoticonSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_EMOTICON);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(EmoticonSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMOTICON);
        onCreate(db);
    }
}
