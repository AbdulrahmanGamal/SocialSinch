package com.vanniktech.emoji.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.vanniktech.emoji.EmojiImageView;
import com.vanniktech.emoji.ImageLoadingTask;
import com.vanniktech.emoji.R;
import com.vanniktech.emoji.emoji.Emoji;
import com.vanniktech.emoji.listeners.OnEmojiClickedListener;
import com.vanniktech.emoji.listeners.OnEmojiLongClickedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static com.vanniktech.emoji.UtilsKt.checkNotNull;

public final class EmojiArrayAdapter extends ArrayAdapter<Emoji> {
  @Nullable private final OnEmojiClickedListener listener;
  @Nullable private final OnEmojiLongClickedListener longListener;

  public EmojiArrayAdapter(@NonNull final Context context, @NonNull final Emoji[] emojis,
                    @Nullable final OnEmojiClickedListener listener,
                    @Nullable final OnEmojiLongClickedListener longListener) {
    super(context, 0, new ArrayList<>(Arrays.asList(emojis)));
    this.listener = listener;
    this.longListener = longListener;
  }

  @NonNull
  @Override
  public View getView(final int position, final View convertView, @NonNull final ViewGroup parent) {
    EmojiImageView image = (EmojiImageView) convertView;

    if (image == null) {
        image = (EmojiImageView) LayoutInflater.from(getContext()).inflate(R.layout.emoji_item, parent, false);
    }

    final Emoji emoji = checkNotNull(getItem(position), "emoji == null");

    image.setImageDrawable(null);
    image.setOnClickListener(v -> {
      if (listener != null) {
        listener.onEmojiClicked(getItem(position));
      }
    });

    if (emoji.getBase().hasVariants()) {
      image.setHasVariants(true);
      image.setOnLongClickListener(v -> {
        if (longListener != null) {
          longListener.onEmojiLongClicked(v, emoji);

          return true;
        }

        return false;
      });
    } else {
      image.setHasVariants(false);
      image.setOnLongClickListener(null);
    }

    ImageLoadingTask task = (ImageLoadingTask) image.getTag();

    if (task != null) {
       task.cancel(true);
    }

    task = new ImageLoadingTask(image);
    image.setTag(task);
    task.execute(emoji.getResource());

    return image;
  }

  public void updateEmojis(final Collection<Emoji> emojis) {
    clear();
    addAll(emojis);
    notifyDataSetChanged();
  }
}
