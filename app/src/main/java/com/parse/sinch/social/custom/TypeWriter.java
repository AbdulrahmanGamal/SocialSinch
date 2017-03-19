package com.parse.sinch.social.custom;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

public class TypeWriter extends android.support.v7.widget.AppCompatTextView {

    private CharSequence mTextToHide, mTextToDisplay;
    private int mIndex;
    private long mInitialDelay = 2000; //Default 500ms delay
    private long mDelay = 500; //Default 500ms delay
    private Handler mHandler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            if (mTextToHide != null
                    && mTextToDisplay != null && mIndex < mTextToHide.length()) {
                Log.d("Typewriter", "mIndex = " + mIndex);
                setText(mTextToDisplay.subSequence(mIndex++, mTextToDisplay.length()));
                mHandler.postDelayed(characterAdder, mDelay);
            }
        }
    };

    public TypeWriter(Context context) {
        super(context);
    }

    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TypeWriter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void animateText(CharSequence textToHide, String textToDisplay) {
        mTextToHide = textToHide;
        mTextToDisplay = textToDisplay;
        mIndex = 0;

        setText(textToDisplay);
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mInitialDelay);
    }

    public void setCharacterDelay(long millis) {
        mDelay = millis;
    }

    public void setInitialDelay(long millis) {
        mInitialDelay = millis;
    }
}
