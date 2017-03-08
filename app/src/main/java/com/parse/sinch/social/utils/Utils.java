package com.parse.sinch.social.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

public class Utils {

    private static final String TAG = "Utils";

    @SuppressLint("NewApi")
	public static String getRealPathFromURI_API19(Context context, Uri uri){
		String filePath = "";
		String wholeID = DocumentsContract.getDocumentId(uri);

		// Split at colon, use second item in the array
		String id = wholeID.split(":")[1];

		String[] column = { MediaStore.Images.Media.DATA };     

		// where id is equal to             
		String sel = MediaStore.Images.Media._ID + "=?";

		Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
				column, sel, new String[]{ id }, null);

		int columnIndex = cursor.getColumnIndex(column[0]);

		if (cursor.moveToFirst()) {
			filePath = cursor.getString(columnIndex);
		}   
		cursor.close();
		return filePath;
	}


	@SuppressLint("NewApi")
	public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		String result = null;

		CursorLoader cursorLoader = new CursorLoader(
				context, 
				contentUri, proj, null, null, null);        
		Cursor cursor = cursorLoader.loadInBackground();

		if(cursor != null){
			int column_index = 
					cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			result = cursor.getString(column_index);
		}
		return result;  
	}

	public static Bitmap getResizedBitmap(Context context, String imagePath, int imageMaxSize){

		try
		{
			File f = new File(imagePath);
			//Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f),null,o);

			//Find the correct scale value. It should be the power of 2.
			int scale=1;
			while(o.outWidth/scale/2>=imageMaxSize && o.outHeight/scale/2>=imageMaxSize)
				scale*=2;

			//Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize=scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);

		}
		catch (IOException e)
		{
			Log.e("Image", e.getMessage(), e);
		}

		return null;
	}

//	public static String convertDateToString(@NonNull Date messageDate) {
//        final SimpleDateFormat dateFormat = new SimpleDateFormat(
//                "EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
//        return dateFormat.format(messageDate);
//	}
//
//    public static Date convertStringToDate(@NonNull String messageDate) {
//        try {
//            final SimpleDateFormat dateFormat = new SimpleDateFormat(
//                    "EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
//            return dateFormat.parse(messageDate);
//        } catch (final ParseException e) {
//            Log.e(TAG, "Parsing exception: " + e.getMessage());
//        }
//
//        return null;
//    }
}
