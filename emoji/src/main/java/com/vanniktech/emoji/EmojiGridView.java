package com.vanniktech.emoji;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.vanniktech.emoji.adapter.EmojiArrayAdapter;
import com.vanniktech.emoji.custom.BasicGridView;
import com.vanniktech.emoji.emoji.EmojiCategory;
import com.vanniktech.emoji.gif.EndlessScrollListener;
import com.vanniktech.emoji.listeners.OnEmojiClickedListener;
import com.vanniktech.emoji.listeners.OnEmojiLongClickedListener;

@SuppressLint("ViewConstructor")
public class EmojiGridView extends BasicGridView {
  protected EmojiArrayAdapter emojiArrayAdapter;

  public EmojiGridView(final Context context, Toolbar toolbarBottom) {
    super(context, toolbarBottom);

    final int width = getResources().getDimensionPixelSize(R.dimen.emoji_grid_view_column_width);
    final int spacing = getResources().getDimensionPixelSize(R.dimen.emoji_grid_view_spacing);

    setColumnWidth(width);
    setHorizontalSpacing(spacing);
    setVerticalSpacing(spacing);
    setPadding(spacing, spacing, spacing, spacing);
    setNumColumns(AUTO_FIT);
    setClipToPadding(false);
    setVerticalScrollBarEnabled(false);
    setOnScrollListener(new EndlessScrollListener() {
      @Override
      public boolean onLoadMore(int page, int totalItemsCount) {
        return false;
      }
    });
  }

  public EmojiGridView init(@Nullable final OnEmojiClickedListener onEmojiClickedListener,
                            @Nullable final OnEmojiLongClickedListener onEmojiLongClickedListener,
                            @NonNull final EmojiCategory category) {
    emojiArrayAdapter = new EmojiArrayAdapter(getContext(), category.getEmojis(), onEmojiClickedListener, onEmojiLongClickedListener);
    setAdapter(emojiArrayAdapter);
    return this;
  }
}
