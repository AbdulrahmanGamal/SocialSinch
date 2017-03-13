package com.parse.sinch.social.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.backendless.Backendless;
import com.parse.sinch.social.R;
import com.parse.sinch.social.databinding.IncomingChatMessageBinding;
import com.parse.sinch.social.databinding.OutgoingChatMessageBinding;
import com.parse.sinch.social.viewmodel.ChatIncomingViewModel;
import com.parse.sinch.social.viewmodel.ChatOutgoingViewModel;
import com.social.sinchservice.SinchServiceConnection;
import com.social.sinchservice.bus.RxOutgoingMessageBus;
import com.social.sinchservice.model.ChatMessage;
import com.social.sinchservice.model.ChatStatus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Adapter in charge of adding the views with the incoming/outgoing chat messages in the mmain
 * recycler view.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.BindingHolder> {

    private Context mContext;
    private List<ChatMessage> mMessages;
    private NewItemInserted mItemInsertedListener;
    private SinchServiceConnection mServiceConnection;

    private static final String TAG = "ChatMessageAdapter";

    private static final int MESSAGE_OUTGOING = 1;
    private static final int MESSAGE_INCOMING = 2;

    public ChatMessageAdapter(Context context,
                              String senderId,
                              List<String> recipientIds,
                              NewItemInserted itemInsertedListener) {
        this.mContext = context;
        this.mMessages = new ArrayList<>();
        this.mItemInsertedListener = itemInsertedListener;
        this.mServiceConnection = SinchServiceConnection.getInstance(context, senderId);

        setHasStableIds(true);
        configureMessageBus();
        retrieveOldMessages(senderId, recipientIds);
    }

    /**
     * Attach this class with the bus so it can receive notification about the
     * messages sent and received
     */
    private void configureMessageBus() {
        RxOutgoingMessageBus.getInstance().getMessageObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ChatMessage>() {
            @Override
            public void accept(ChatMessage chatInfo) throws Exception {
                if (chatInfo.getStatus().equals(ChatStatus.RECEIVED)) {
                    Log.e(TAG, "RECEIVED MESSAGE FROM BUS!!!");
                    addMessage(chatInfo);
                } else if (!mMessages.isEmpty()) {
                       boolean found = false;
                        for (int i = mMessages.size() -1; i >=0; i--) {
                            ChatMessage chatInList = mMessages.get(i);
                            if (chatInList.getTextBody().equals(chatInfo.getTextBody())) {
                                switch (chatInfo.getStatus()) {
                                    case SENT:
                                        chatInList.setResourceId(R.drawable.message_got_receipt_from_server);
                                        notifyItemChanged(i);
                                        break;
                                    case DELIVERED:
                                        chatInList.setResourceId(R.drawable.message_got_receipt_from_target);
                                        notifyItemChanged(i);
                                    default:
                                        break;
                                }
                                found = true;
                                break;
                            }
                        }
                    if (!found) {
                        Log.e(TAG, "MESSAGE NOT IN LIST BUT RSEND OR DELIVERED WAS EMITTED");
                        switch (chatInfo.getStatus()) {
                            case SENT:
                                chatInfo.setResourceId(R.drawable.message_got_receipt_from_server);
                                addMessage(chatInfo);
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    Log.e(TAG, "chat list EMPY, ADDING MESSAGE");
                    addMessage(chatInfo);
                }
            }
        });
    }

    /**
     * Onece the adapter is intantiate, it has to retrieve the old messages saved in DB
     * @param senderId
     * @param recipientIds
     */
    private synchronized void retrieveOldMessages(final String senderId, List<String> recipientIds) {
        String recipientId = recipientIds.get(0);
        List<ChatMessage> chatMessages = mServiceConnection.retrieveLastMessages(senderId,
                                                                                recipientId, 100);
        for (ChatMessage oldChatMessages : chatMessages) {
            if (oldChatMessages.getSenderId().equals(senderId)) {
                oldChatMessages.setStatus(ChatStatus.DELIVERED);
                oldChatMessages.setResourceId(R.drawable.message_got_read_receipt_from_target);
            } else {
                oldChatMessages.setStatus(ChatStatus.RECEIVED);
            }
            mMessages.add(oldChatMessages);
        }
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
        if (mMessages.get(position).getStatus().equals(ChatStatus.RECEIVED)) {
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

    public synchronized void addMessage(final ChatMessage chatMessage) {
        if (chatMessage.getStatus().equals(ChatStatus.WAITING)) {
            chatMessage.setMessageId((long)(getItemCount() + 1));
            Log.e(TAG, "SENDING MESSAGE TO SINCH");
            mServiceConnection.sendMessage(chatMessage.getRecipientIds(), chatMessage.getTextBody());
        }

        mMessages.add(chatMessage);
        notifyItemInserted(mMessages.size());
        //notify the recycler view to scroll up the recycler view
        mItemInsertedListener.onItemInserted();
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public long getItemId(int position) {
        return mMessages.get(position).getMessageId();
    }

    public void removeMessageClientListener() {
        mServiceConnection.unbindService(mContext);
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

    public interface NewItemInserted {
        void onItemInserted();
    }
}
