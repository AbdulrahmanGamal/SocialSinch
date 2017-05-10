package com.social.valgoodchat.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.social.backendless.model.ChatStatus;
import com.social.valgoodchat.R;
import com.social.valgoodchat.custom.TypeWriter;
import com.social.valgoodchat.model.ViewMessage;

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

	public static Bitmap getResizedBitmap(String imagePath, int imageMaxSize){

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

	/**
	 * Change the resource icon based on the message's status
	 * @param viewMessage
	 */
	public static void changeStatusIcon(ViewMessage viewMessage, ChatStatus status) {
		switch (status) {
			case SENT:
				viewMessage.setResourceId(R.drawable.message_got_receipt_from_server);
				break;
			case DELIVERED:
				viewMessage.setResourceId(R.drawable.message_got_receipt_from_target);
				break;
			case RECEIVED:
			case READ:
			case SENT_READ:
				viewMessage.setResourceId(R.drawable.message_got_read_receipt_from_target);
				break;
			case WAITING:
			case FAILED:
			default:
				viewMessage.setResourceId(R.drawable.message_waiting);
				break;
		}
	}
	/**
	 * Set the last time seen text and animate it
	 * @param lastTimeSeen
	 * @param textView
	 */
	public static void formatLastTimeSeenText(Context context, String lastTimeSeen, TypeWriter textView) {
		// Start after 2000ms
		textView.setInitialDelay(2000);
		// Remove a character every 150ms
		textView.setCharacterDelay(1);
		String lastTimeSeenText = context.getResources().getString(R.string.last_time_seen) + " ";

		StringBuilder outputFormat = new StringBuilder();
		if (lastTimeSeen.contains("today")) {
			lastTimeSeen = lastTimeSeen.replace("today at", "");
			outputFormat.append(context.getResources().getString(R.string.today)).append(" ").
					append(context.getResources().getString(R.string.at));
		} else if (lastTimeSeen.contains("yesterday")) {
			lastTimeSeen = lastTimeSeen.replace("yesterday at", "");
			outputFormat.append(context.getResources().getString(R.string.yesterday)).append(" ").
					append(context.getResources().getString(R.string.at));
		}
		outputFormat.append(lastTimeSeen);
		textView.animateText(lastTimeSeenText, lastTimeSeenText + outputFormat.toString());
	}
}
