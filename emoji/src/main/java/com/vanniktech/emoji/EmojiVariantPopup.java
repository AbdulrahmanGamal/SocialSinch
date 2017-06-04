package com.vanniktech.emoji;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.vanniktech.emoji.emoji.Emoji;
import com.vanniktech.emoji.listeners.OnEmojiClickedListener;

import java.util.List;

import jp.wasabeef.glide.transformations.internal.Utils;

import static android.view.View.MeasureSpec.makeMeasureSpec;
import static com.vanniktech.emoji.UtilsKt.dpToPx;
import static com.vanniktech.emoji.UtilsKt.fixPopupLocation;
import static com.vanniktech.emoji.UtilsKt.locationOnScreen;

public final class EmojiVariantPopup {

  private static final int MARGIN = 2;
  @NonNull private final View rootView;
  @Nullable private final OnEmojiClickedListener listener;
  @Nullable private PopupWindow popupWindow;

  public EmojiVariantPopup(@NonNull final View rootView, @Nullable final OnEmojiClickedListener listener) {
    this.rootView = rootView;
    this.listener = listener;
  }

  public void show(@NonNull final View clickedImage, @NonNull final Emoji emoji) {
    dismiss();

    final View content = initView(clickedImage.getContext(), emoji, clickedImage.getWidth());

    popupWindow = new PopupWindow(content, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    popupWindow.setFocusable(true);
    popupWindow.setOutsideTouchable(true);
    popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
    popupWindow.setBackgroundDrawable(new BitmapDrawable(clickedImage.getContext().getResources(), (Bitmap) null));

    content.measure(makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

    final Point location = locationOnScreen(clickedImage);
    final Point desiredLocation = new Point(
            location.x - content.getMeasuredWidth() / 2 + clickedImage.getWidth() / 2,
            location.y - content.getMeasuredHeight()
    );

    popupWindow.showAtLocation(rootView, Gravity.NO_GRAVITY, desiredLocation.x, desiredLocation.y);
    fixPopupLocation(popupWindow, desiredLocation);
  }

 public void dismiss() {
    if (popupWindow != null) {
      popupWindow.dismiss();
      popupWindow = null;
    }
  }

  private View initView(@NonNull final Context context, @NonNull final Emoji emoji, final int width) {
    final View result = View.inflate(context, R.layout.emoji_skin_popup, null);
    final LinearLayout imageContainer = (LinearLayout) result.findViewById(R.id.container);

    final List<Emoji> variants = emoji.getBase().getVariants();
    variants.add(0, emoji.getBase());

    final LayoutInflater inflater = LayoutInflater.from(context);

    for (final Emoji variant : variants) {
      final ImageView emojiImage = (ImageView) inflater.inflate(R.layout.emoji_item, imageContainer, false);
      final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) emojiImage.getLayoutParams();
      final int margin = dpToPx(context, MARGIN);

      // Use the same size for Emojis as in the picker.
      layoutParams.width = width;
      layoutParams.setMargins(margin, margin, margin, margin);
      emojiImage.setImageResource(variant.getResource());

      emojiImage.setOnClickListener(view -> {
        if (listener != null) {
          listener.onEmojiClicked(variant);
        }
      });

      imageContainer.addView(emojiImage);
    }

    return result;
  }
}
