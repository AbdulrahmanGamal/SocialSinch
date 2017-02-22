package com.parse.sinch.social.viewmodel;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.sinch.social.adapter.ChatMessageAdapter;
import com.parse.sinch.social.service.SinchServiceConnection;

/**
 * Created by valgood on 2/19/2017.
 */

public class MessageViewModel {
    private Context mContext;
    private String mRecipientId;
    private String mCurrentUserId;
    private ChatMessageAdapter mChatMessageAdapter;
    private SinchServiceConnection mConnection;

    private EditText mMessage;

    public MessageViewModel(Context context, String recipientId, String currentUserId) {
        this.mContext = context;
        this.mRecipientId = recipientId;
        this.mCurrentUserId = currentUserId;
        this.mChatMessageAdapter = new ChatMessageAdapter(context);
        this.mConnection = new SinchServiceConnection(context);
    }

    public View.OnClickListener onClickSend() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMessage.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "Please enter a message", Toast.LENGTH_LONG).show();
                    return;
                }
                mConnection.sendMessage(mRecipientId, mMessage.getText().toString());
                mMessage.setText("");;
            }
        };
    }

    public void setMessage(EditText message) {
        this.mMessage = message;
    }

    @BindingAdapter("android:text")
    public static void setMessageEditText(EditText messageEditText, MessageViewModel viewModel) {
        viewModel.setMessage(messageEditText);
    }

    @BindingAdapter("chatViewModel")
    public static void setUserCallViewModel(RecyclerView recyclerView,
                                            MessageViewModel viewModel) {
        //viewModel.getUserCalls(recyclerView);
        recyclerView.setAdapter(viewModel.getAdapter());
        recyclerView.setLayoutManager(viewModel.createLayoutManager());
    }
    private RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(mContext);
    }
    public ChatMessageAdapter getAdapter() {
        return mChatMessageAdapter;
    }

    public void removeMessageClientListener() {
        mConnection.removeMessageClientListener();
    }
}
