package com.social.valgoodchat.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * Extending EmojiEditText to catch the back button press and hide the emoji pop up
 */

public class EmojiEditText extends com.vanniktech.emoji.EmojiEditText {
    private KeyImeChange keyImeChangeListener;

    public EmojiEditText(Context context) {
        super(context);
    }

    public EmojiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setKeyImeChangeListener(KeyImeChange listener){
        keyImeChangeListener = listener;
    }

    public interface KeyImeChange {
        void onKeyIme(int keyCode, KeyEvent event);
    }

    @Override
    public boolean onKeyPreIme (int keyCode, KeyEvent event){
        if(keyImeChangeListener != null){
            keyImeChangeListener.onKeyIme(keyCode, event);
        }
        return false;
    }
}
