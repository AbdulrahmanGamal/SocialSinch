package com.vanniktech.emoji.window;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
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

import com.vanniktech.emoji.Utils;
import com.vanniktech.emoji.gif.GIFView;

/**
 * Pop up to display the animated GIfs
 */

public class EmojiGifPopup {
    private PopupWindow mPopupWindow;
    private View mRootView;
    private int mKeyBoardHeight;
    private boolean isPendingOpen;
    private boolean isKeyboardOpen;

    private static final int MIN_KEYBOARD_HEIGHT = 100;

    private final ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override public void onGlobalLayout() {
            final Context context = mRootView.getContext();
            final Rect rect = new Rect();
            mRootView.getWindowVisibleDisplayFrame(rect);

            final int heightDifference = getUsableScreenHeight() - rect.bottom;

            if (heightDifference > Utils.dpToPx(context, MIN_KEYBOARD_HEIGHT)) {
                mPopupWindow.setHeight(heightDifference);
                mPopupWindow.setWidth(rect.right);

                isKeyboardOpen = true;

                if (isPendingOpen) {
                    showAtBottom();
                    isPendingOpen = false;
                }
            } else {
                if (isKeyboardOpen) {
                    isKeyboardOpen = false;

                    dismiss();
                    mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
                }
            }
        }
    };

    public EmojiGifPopup(@NonNull final View rootView) {
        this.mRootView = rootView;

        mPopupWindow = new PopupWindow(mRootView.getContext());
        // To avoid borders & overdraw
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(mRootView.getContext().getResources(), (Bitmap) null));
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        //mPopupWindow.setHeight((int) mRootView.getContext().
          //                          getResources().getDimension(com.vanniktech.emoji.R.dimen.emoji_keyboard_height));

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
            // Remove any previous listeners to avoid duplicates.
            mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
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

    public void closeSoftKeyboard(EditText emojiEditText) {
        if (emojiEditText.hasFocus()) {
            InputMethodManager imm =
                    (InputMethodManager) emojiEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(emojiEditText.getWindowToken(), 0);
            isKeyboardOpen = false;
        }
    }

    public boolean isKeyboardOpen() {
        return isKeyboardOpen;
    }
    public boolean isShowing() {
        return mPopupWindow.isShowing();
    }

    void showAtBottom() {
        mPopupWindow.setContentView(new GIFView(mRootView.getContext()));
        final Point desiredLocation = new Point(0, getUsableScreenHeight() - mPopupWindow.getHeight());

        mPopupWindow.showAtLocation(mRootView, Gravity.NO_GRAVITY, desiredLocation.x, desiredLocation.y);
        Utils.fixPopupLocation(mPopupWindow, desiredLocation);
        mPopupWindow.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
    }

    public void dismiss() {
        mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        isKeyboardOpen = false;
        mRootView.getHandler().postDelayed(() -> {
            mPopupWindow.dismiss();
        }, 400);
    }
}
