package com.vanniktech.emoji.listeners;

/**
 * Events fired when the scrolling moves either in the emoji window or gif window
 */

public interface OnScrollListener {
    void onScrollingUp();
    void onScrollingDown();
    void onLoadingStarted();
    void onLoadFinished();
}
