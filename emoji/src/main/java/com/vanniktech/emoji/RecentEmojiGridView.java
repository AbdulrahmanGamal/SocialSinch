package com.vanniktech.emoji;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.vanniktech.emoji.adapter.EmojiArrayAdapter;
import com.vanniktech.emoji.emoji.Emoji;
import com.vanniktech.emoji.listeners.OnEmojiClickedListener;
import com.vanniktech.emoji.listeners.OnEmojiLongClickedListener;

import java.util.Collection;

@SuppressLint("ViewConstructor")
final class RecentEmojiGridView extends EmojiGridView {
  private RecentEmoji recentEmojis;

  RecentEmojiGridView(@NonNull final Context context, @NonNull Toolbar toolbarBottom) {
    super(context, toolbarBottom);
  }

  public RecentEmojiGridView init(@Nullable final OnEmojiClickedListener onEmojiClickedListener,
                                  @Nullable final OnEmojiLongClickedListener onEmojiLongClickedListener,
                                  @NonNull final RecentEmoji recentEmoji) {
    recentEmojis = recentEmoji;
    final Collection<Emoji> emojis = recentEmojis.getRecentEmojis();
    emojiArrayAdapter = new EmojiArrayAdapter(getContext(), emojis.
                                toArray(new Emoji[emojis.size()]), onEmojiClickedListener, onEmojiLongClickedListener);
    setAdapter(emojiArrayAdapter);
    return this;
  }

  public void invalidateEmojis() {
    emojiArrayAdapter.updateEmojis(recentEmojis.getRecentEmojis());
  }
}
