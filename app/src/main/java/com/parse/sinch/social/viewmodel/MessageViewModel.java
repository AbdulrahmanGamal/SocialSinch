package com.parse.sinch.social.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.sinch.social.TenorGridActivity;
import com.parse.sinch.social.adapter.ChatMessageAdapter;

/**
 * Model View attached to the Incoming/outgoing chat messages
 */

public class MessageViewModel implements ChatMessageAdapter.NewItemInserted {
    private Context mContext;
    private ChatMessageAdapter mChatMessageAdapter;
    private EditText mMessage;
    private RecyclerView mChatRecyclerView;
    private String mRecipientId;

    public MessageViewModel(Context context, String recipientInfo) {
        this.mContext = context;
        this.mRecipientId = recipientInfo;
        this.mChatMessageAdapter = new ChatMessageAdapter(context, recipientInfo, this);
    }

    public View.OnClickListener onClickSend() {
        return v -> {
            if (mMessage.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "Please enter a message", Toast.LENGTH_LONG).show();
                return;
            }
            mChatMessageAdapter.sendMessage(mRecipientId, mMessage.getText().toString());
            mMessage.setText("");
        };
    }

    public View.OnClickListener onClickEmoji() {
        return v -> mContext.startActivity(new Intent(mContext, TenorGridActivity.class));
    }
    public void setMessage(EditText message) {
        this.mMessage = message;
    }

    @BindingAdapter("android:text")
    public static void setMessageEditText(EditText messageEditText, MessageViewModel viewModel) {
        viewModel.setMessage(messageEditText);
    }

    @BindingAdapter("chatViewModel")
    public static void setUserCallViewModel(final RecyclerView recyclerView,
                                            final MessageViewModel viewModel) {
        viewModel.setChatRecyclerView(recyclerView);
        recyclerView.setAdapter(viewModel.getAdapter());
        recyclerView.setLayoutManager(viewModel.createLayoutManager());
        recyclerView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                viewModel.onItemInserted();
            }
        });
    }
    private RecyclerView.LayoutManager createLayoutManager() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setStackFromEnd(true);
        return manager;
    }
    private ChatMessageAdapter getAdapter() {
        return mChatMessageAdapter;
    }

    public void setChatRecyclerView(RecyclerView recyclerView) {
        this.mChatRecyclerView = recyclerView;
    }

    @Override
    public void onItemInserted() {
        if (mChatRecyclerView != null) {
            mChatRecyclerView.postDelayed(() -> {
                if (mChatRecyclerView.getAdapter().getItemCount() > 0) {
                    mChatRecyclerView.smoothScrollToPosition(
                            mChatRecyclerView.getAdapter().getItemCount() - 1);
                }
            }, 100);
        }
    }
}
