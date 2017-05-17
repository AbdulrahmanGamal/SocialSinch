package com.social.valgoodchat.custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.vanniktech.emoji.*;

/**
 * Pop up to display the animated GIfs
 */

public class GifPopup {
    private PopupWindow mPopupWindow;
    private View mRootView;
    private int mKeyBoardHeight;
    private boolean isPendingOpen;
    private boolean isKeyboardOpen;

    private static final int MIN_KEYBOARD_HEIGHT = 100;

    private final ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override public void onGlobalLayout() {
            final Rect rect = new Rect();
            mRootView.getWindowVisibleDisplayFrame(rect);

            int heightDifference = getUsableScreenHeight() - (rect.bottom - rect.top);

            final Resources resources = mRootView.getContext().getResources();
            final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");

            if (resourceId > 0) {
                heightDifference -= resources.getDimensionPixelSize(resourceId);
            }

            if (heightDifference > MIN_KEYBOARD_HEIGHT) {
                mKeyBoardHeight = heightDifference;
                mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
                mPopupWindow.setHeight(mKeyBoardHeight);

                isKeyboardOpen = true;

                if (isPendingOpen) {
                    mPopupWindow.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
                    isPendingOpen = false;
                }
            } else {
                if (isKeyboardOpen) {
                    isKeyboardOpen = false;
                }
            }
        }
    };

    public GifPopup(@NonNull final View rootView) {
        this.mRootView = rootView;

        mPopupWindow = new PopupWindow(mRootView.getContext());
        // To avoid borders & overdraw
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(mRootView.getContext().getResources(), (Bitmap) null));
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight((int) mRootView.getContext().getResources().getDimension(R.dimen.emoji_keyboard_height));

    }

    private int getUsableScreenHeight() {
        final DisplayMetrics metrics = new DisplayMetrics();
        final WindowManager windowManager =
                (WindowManager) mRootView.getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        return metrics.heightPixels;
    }

    public void toggle(EditText emojiEditText) {
        if (!mPopupWindow.isShowing()) {
            mRootView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);

            if (isKeyboardOpen) {
                // If keyboard is visible, simply show the emoji popup
                showAtBottom();
            } else {
                // Open the text keyboard first and immediately after that show the emoji popup
                emojiEditText.setFocusableInTouchMode(true);
                emojiEditText.requestFocus();

                showAtBottomPending();

                final InputMethodManager inputMethodManager =
                        (InputMethodManager) mRootView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(emojiEditText, InputMethodManager.SHOW_IMPLICIT);
            }

        } else {
            dismiss();
        }

        // Manually dispatch the event. In some cases this does not work out of the box reliably.
        mRootView.getViewTreeObserver().dispatchOnGlobalLayout();
    }

    private void showAtBottomPending() {
        if (isKeyboardOpen) {
            showAtBottom();
        } else {
            isPendingOpen = true;
        }
    }

    public boolean isShowing() {
        return mPopupWindow.isShowing();
    }

    void showAtBottom() {
        mPopupWindow.setContentView(new GIFView(mRootView.getContext()));
        mPopupWindow.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
    }

    public void dismiss() {
        mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        mPopupWindow.dismiss();
    }
}
