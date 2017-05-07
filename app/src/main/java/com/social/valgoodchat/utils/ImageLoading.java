package com.social.valgoodchat.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.social.valgoodchat.R;

import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Encapsulates the logic to cache and display images
 */

public class ImageLoading {

    private static final String TAG = "ImageLoading";
    /**
     * Load a URL image with glide
     * @param profileURL
     * @param imageView
     */
    public static void loadContactPicture(String profileURL, ImageView imageView) {
        Glide.with(imageView.getContext()).load(profileURL)
                .bitmapTransform(new CropCircleTransformation(imageView.getContext()))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }

    public static Bitmap getPictureForNotification(Context context, String pictureURL) {
        Bitmap roundedBitmap = null;
        try {
            Bitmap srcBitmap = Glide.with(context.getApplicationContext()) // safer!
                    .load(pictureURL)
                    .asBitmap().into(100,100).get();

            roundedBitmap = ImageLoading.getCircleBitmap(srcBitmap);
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "Error getting profile pic with glide");
        }

        if (roundedBitmap == null) {
            roundedBitmap = BitmapFactory.
                    decodeResource(context.getResources(), R.drawable.ic_launcher);
        }
        return roundedBitmap;
    }
    /**
     * Circle the notification bitmap
     * @param bitmap
     * @return
     */
    private static Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        //If the bitmap gets recycled an exception occurs every time a new offline message comes in
        //bitmap.recycle();

        return output;
    }
}
