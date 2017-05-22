package com.vanniktech.emoji.listeners;

import android.view.View;
import com.vanniktech.emoji.emoji.Emoji;

public interface OnEmojiLongClickedListener {
  void onEmojiLongClicked(View view, Emoji emoji);
}
