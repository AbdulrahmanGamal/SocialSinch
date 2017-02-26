package com.parse.sinch.social.viewmodel;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.parse.sinch.social.adapter.ChatMessageAdapter;
import com.parse.sinch.social.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Model View attached to the Incoming/outgoing chat messages
 */

public class MessageViewModel {
    private Context mContext;
    private String mRecipientId;
    private ChatMessageAdapter mChatMessageAdapter;
    private EditText mMessage;

    public MessageViewModel(Context context, String recipientId) {
        this.mContext = context;
        this.mRecipientId = recipientId;
        this.mChatMessageAdapter = new ChatMessageAdapter(context);
    }

    public View.OnClickListener onClickSend() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMessage.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "Please enter a message", Toast.LENGTH_LONG).show();
                    return;
                }

                mChatMessageAdapter.addMessage(assembleChatToSend());
                mMessage.setText("");
            }
        };
    }

    /**
     * Method to wrap the information into a Chat Message
     * @return
     */
    private ChatMessage assembleChatToSend() {
        ChatMessage chatToSend = new ChatMessage();
        String currentUserId = Backendless.UserService.loggedInUser();
        chatToSend.setStatus(ChatMessage.ChatStatus.WAITING);
        chatToSend.setMessageId(currentUserId);
        chatToSend.setTextBody(mMessage.getText().toString());
        chatToSend.setSenderId(currentUserId);
        List<String> recipientIds = new ArrayList<>();
        recipientIds.add(mRecipientId);
        chatToSend.setResourceId(android.R.drawable.ic_menu_compass);
        chatToSend.setRecipientIds(recipientIds);
        return chatToSend;
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
        recyclerView.setAdapter(viewModel.getAdapter());
        recyclerView.setLayoutManager(viewModel.createLayoutManager());
    }
    private RecyclerView.LayoutManager createLayoutManager() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setStackFromEnd(true);
        return manager;
    }
    private ChatMessageAdapter getAdapter() {
        return mChatMessageAdapter;
    }

    public void removeMessageClientListener() {
        mChatMessageAdapter.removeMessageClientListener();
    }
}
