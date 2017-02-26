package com.parse.sinch.social.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.sinch.social.R;
import com.parse.sinch.social.databinding.IncomingChatMessageBinding;
import com.parse.sinch.social.databinding.OutgoingChatMessageBinding;
import com.parse.sinch.social.model.ChatMessage;
import com.parse.sinch.social.model.RxOutgoingMessageBus;
import com.parse.sinch.social.service.ServiceConnectionManager;
import com.parse.sinch.social.viewmodel.ChatIncomingViewModel;
import com.parse.sinch.social.viewmodel.ChatOutgoingViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Adapter in charge of adding the views with the incoming/outgoing chat messages in the mmain
 * recycler view.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.BindingHolder> {

    private Context mContext;
    private List<ChatMessage> mMessages;
    private ServiceConnectionManager mServiceConnection;

    private static final int MESSAGE_OUTGOING = 1;
    private static final int MESSAGE_INCOMING = 2;

    public ChatMessageAdapter(Context context) {
        this.mContext = context;
        this.mMessages = new ArrayList<>();
        this.mServiceConnection = new ServiceConnectionManager(context);

        //attach this class with the bus so it can receive notification about the
        //messages sent and received
        RxOutgoingMessageBus.getInstance().getMessageObservable().subscribe(new Consumer<ChatMessage>() {
            @Override
            public void accept(ChatMessage chatInfo) throws Exception {
                for (int i = mMessages.size() -1; i >=0; i--){
                    ChatMessage chatInList = mMessages.get(i);
                    if (chatInList.getTextBody().equals(chatInfo.getTextBody())) {
                        switch (chatInfo.getStatus()) {
                            case SENT:
                                chatInList.setResourceId(R.drawable.message_got_receipt_from_server);
                                break;
                            case DELIVERED:
                                chatInList.setResourceId(R.drawable.message_got_receipt_from_target);
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MESSAGE_INCOMING) {
            IncomingChatMessageBinding incomingChatMessageBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.incoming_chat_message, parent, false);
            return new IncomingBindingHolder(incomingChatMessageBinding);
        } else {
            OutgoingChatMessageBinding outgoingChatMessageBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.outgoing_chat_message, parent, false);
            return new OutgoingBindingHolder(outgoingChatMessageBinding);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mMessages.get(position).getStatus().equals(ChatMessage.ChatStatus.RECEIVED)) {
            return MESSAGE_INCOMING;
        }
        return MESSAGE_OUTGOING;
    }
    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        if (holder instanceof IncomingBindingHolder) {
            IncomingChatMessageBinding binding =
                    ((IncomingBindingHolder) holder).getIncomingChatMessageBinding();
            binding.setViewModel(new ChatIncomingViewModel(mContext, mMessages.get(position)));
        } else {
            OutgoingChatMessageBinding binding =
                    ((OutgoingBindingHolder) holder).getOutgoingChatMessageBinding();
            binding.setViewModel(new ChatOutgoingViewModel(mMessages.get(position)));
        }
    }

    public void addMessage(ChatMessage chatMessage) {
        mMessages.add(chatMessage);
        notifyDataSetChanged();
        mServiceConnection.sendMessage(chatMessage.getRecipientIds(), chatMessage.getTextBody());
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public void removeMessageClientListener() {
        mServiceConnection.removeMessageClientListener(mContext);
    }

    public class BindingHolder extends RecyclerView.ViewHolder {
        public BindingHolder(View itemView) {
            super(itemView);
        }
    }
    private class IncomingBindingHolder extends BindingHolder {
        private IncomingChatMessageBinding mIncomingChatMessageBinding;

        public IncomingBindingHolder(IncomingChatMessageBinding incomingChatMessageBinding) {
            super(incomingChatMessageBinding.getRoot());
            this.mIncomingChatMessageBinding = incomingChatMessageBinding;
        }

        public IncomingChatMessageBinding getIncomingChatMessageBinding() {
            return mIncomingChatMessageBinding;
        }
    }

    private class OutgoingBindingHolder extends BindingHolder {
        private OutgoingChatMessageBinding mOutgoingChatMessageBinding;

        public OutgoingBindingHolder(OutgoingChatMessageBinding outgoingChatMessageBinding) {
            super(outgoingChatMessageBinding.getRoot());
            this.mOutgoingChatMessageBinding = outgoingChatMessageBinding;
        }

        public OutgoingChatMessageBinding getOutgoingChatMessageBinding() {
            return mOutgoingChatMessageBinding;
        }
    }
}
