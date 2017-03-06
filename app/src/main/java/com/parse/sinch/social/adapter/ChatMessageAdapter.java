package com.parse.sinch.social.adapter;

import android.content.Context;
import android.database.SQLException;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.sinch.social.R;
import com.parse.sinch.social.database.ChatBriteDataSource;
import com.parse.sinch.social.database.ChatDataSource;
import com.parse.sinch.social.databinding.IncomingChatMessageBinding;
import com.parse.sinch.social.databinding.OutgoingChatMessageBinding;
import com.parse.sinch.social.model.ChatMessage;
import com.parse.sinch.social.bus.RxOutgoingMessageBus;
import com.parse.sinch.social.service.ServiceConnectionManager;
import com.parse.sinch.social.viewmodel.ChatIncomingViewModel;
import com.parse.sinch.social.viewmodel.ChatOutgoingViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Adapter in charge of adding the views with the incoming/outgoing chat messages in the mmain
 * recycler view.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.BindingHolder> {

    private Context mContext;
    private List<ChatMessage> mMessages;
    private ServiceConnectionManager mServiceConnection;
    private ChatBriteDataSource mChatDataSource;
    private NewItemInserted mItemInsertedListener;

    private static final String TAG = "ChatMessageAdapter";

    private static final int MESSAGE_OUTGOING = 1;
    private static final int MESSAGE_INCOMING = 2;

    public ChatMessageAdapter(Context context,
                              String senderId,
                              List<String> recipientIds,
                              NewItemInserted itemInsertedListener) {
        this.mContext = context;
        this.mMessages = new ArrayList<>();
        this.mServiceConnection = new ServiceConnectionManager(context);
        this.mChatDataSource = new ChatBriteDataSource(context);
        this.mItemInsertedListener = itemInsertedListener;

        setHasStableIds(true);
        configureMessageBus();
        retrieveOldMessages(senderId, recipientIds);
    }

    /**
     * Attach this class with the bus so it can receive notification about the
     * messages sent and received
     */
    private void configureMessageBus() {
        RxOutgoingMessageBus.getInstance().getMessageObservable().subscribe(new Consumer<ChatMessage>() {
            @Override
            public void accept(ChatMessage chatInfo) throws Exception {
                if (chatInfo.getStatus().equals(ChatMessage.ChatStatus.RECEIVED)) {
                    Log.e(TAG, "RECIEVED MESAGE FROM BUSSS!!!");
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
    private void retrieveOldMessages(final String senderId, List<String> recipientIds) {
        String recipientId = recipientIds.get(0);
        List<ChatMessage> chatMessages = mChatDataSource.retrieveLastMessages(senderId, recipientId, 0, 100);
        for (ChatMessage oldChatMessages : chatMessages) {
            if (oldChatMessages.getSenderId().equals(senderId)) {
                oldChatMessages.setStatus(ChatMessage.ChatStatus.DELIVERED);
                oldChatMessages.setResourceId(R.drawable.message_got_read_receipt_from_target);
            } else {
                oldChatMessages.setStatus(ChatMessage.ChatStatus.RECEIVED);
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

    public synchronized void addMessage(final ChatMessage chatMessage) {
        //in case of RECEIVED we have to swap the send id and recipient id because the saving in the
        //database is user1:user2:totalmessage even of user1 is receiving the message
        Log.e(TAG, "ADDING MESSAGE: " + chatMessage);
        Long messageId;

        try {
            if (chatMessage.getStatus().equals(ChatMessage.ChatStatus.WAITING)) {
                String identifier = String.valueOf(chatMessage.getSenderId() + ":" +
                        chatMessage.getRecipientIds().get(0));
                messageId = mChatDataSource.getTotalMessage(identifier);
                chatMessage.setMessageId(messageId + 1);
                mServiceConnection.sendMessage(chatMessage.getRecipientIds(), chatMessage.getTextBody());
            }

            mMessages.add(chatMessage);
            notifyItemInserted(mMessages.size());
            //notify the recycler view to scroll up the recycler view
            mItemInsertedListener.onItemInserted();
        } catch (SQLException ex) {
            Log.e(TAG, "Error adding the new Message: " + ex.getMessage());
        }
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

    public interface NewItemInserted {
        void onItemInserted();
    }
}
