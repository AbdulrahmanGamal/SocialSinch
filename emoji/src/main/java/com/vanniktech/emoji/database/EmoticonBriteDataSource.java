package com.vanniktech.emoji.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import android.support.annotation.NonNull;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;
import java.util.Locale;
import rx.schedulers.Schedulers;

/**
 * Class to handle the interaction with the local SQLite
 */

public class EmoticonBriteDataSource {
    private BriteDatabase mEmoticonBriteDB;
    private static final Object mObjectLock = new Object();
    private static EmoticonBriteDataSource sEmoticonBriteDataSource;

    private static final String TAG = "EmoticonBriteDataSource";

    public static EmoticonBriteDataSource getInstance(Context context) {
        if (sEmoticonBriteDataSource == null) {
            sEmoticonBriteDataSource = new EmoticonBriteDataSource(context);
        }
        return sEmoticonBriteDataSource;
    }

    private EmoticonBriteDataSource(Context context) {
        EmoticonSQLiteHelper mChatDbHelper = new EmoticonSQLiteHelper(context);
        //create and configure sqlbrite
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        mEmoticonBriteDB = sqlBrite.wrapDatabaseHelper(mChatDbHelper, Schedulers.io());
        mEmoticonBriteDB.setLoggingEnabled(true);
    }

    /**
     * Obtains all the recently used animated emoticons
     * @return
     * @throws SQLException
     */
    public List<String> getRecent() throws SQLException {
        List<String> result = new ArrayList<>();
        String queryRecent = String.valueOf("SELECT " +
                EmoticonSQLiteHelper.COLUMN_NAME + " FROM " +
                EmoticonSQLiteHelper.TABLE_EMOTICON + " ORDER BY " +
                EmoticonSQLiteHelper.COLUMN_DATE_ADDED);
        Cursor cursor = mEmoticonBriteDB.query(queryRecent);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                result.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return result;
    }

    /**
     * Adds the animated emoticon url to the emoticon table
     */
    public void addRecent(@NonNull String emoticonURL) {
        synchronized (mObjectLock) {
            mEmoticonBriteDB.execute(
                    "INSERT OR IGNORE INTO " + EmoticonSQLiteHelper.TABLE_EMOTICON +
                            " ( " + EmoticonSQLiteHelper.COLUMN_NAME + ", " +
                            EmoticonSQLiteHelper.COLUMN_DATE_ADDED + " ) VALUES (?, ?) ",
                    emoticonURL, Calendar.getInstance(Locale.getDefault()).getTime().toString());
        }
    }

    /**
     * Obtains the total animated emoticons recently sent
     * @return
     * @throws SQLException
     */
    public int getTotalRecent() throws SQLException {
        int result = 0;
        String queryTotalRecent = String.valueOf("SELECT count(*) FROM " +
                EmoticonSQLiteHelper.TABLE_EMOTICON);
        Cursor cursor = mEmoticonBriteDB.query(queryTotalRecent);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getInt(0);
        }
        cursor.close();
        return result;
    }
}
