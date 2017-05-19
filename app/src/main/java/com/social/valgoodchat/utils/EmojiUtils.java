package com.social.valgoodchat.utils;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by jorgevalbuena on 5/18/17.
 */

public class EmojiUtils {

    private static final int DONT_UPDATE_FLAG = -1;

    public static int dpToPx(@NonNull final Context context, final float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    private @NonNull static Point locationOnScreen(@NonNull final View view) {
        final int[] location = new int[2];

        view.getLocationOnScreen(location);

        return new Point(location[0], location[1]);
    }

    public static void fixPopupLocation(@NonNull final PopupWindow popupWindow, @NonNull final Point desiredLocation) {
        popupWindow.getContentView().post(() -> {
            final Point actualLocation = locationOnScreen(popupWindow.getContentView());

            if (!(actualLocation.x == desiredLocation.x && actualLocation.y == desiredLocation.y)) {
                final int differenceX = actualLocation.x - desiredLocation.x;
                final int differenceY = actualLocation.y - desiredLocation.y;

                final int fixedOffsetX;
                final int fixedOffsetY;

                if (actualLocation.x > desiredLocation.x) {
                    fixedOffsetX = desiredLocation.x - differenceX;
                } else {
                    fixedOffsetX = desiredLocation.x + differenceX;
                }

                if (actualLocation.y > desiredLocation.y) {
                    fixedOffsetY = desiredLocation.y - differenceY;
                } else {
                    fixedOffsetY = desiredLocation.y + differenceY;
                }

                popupWindow.update(fixedOffsetX, fixedOffsetY, DONT_UPDATE_FLAG, DONT_UPDATE_FLAG);
            }
        });
    }
}
