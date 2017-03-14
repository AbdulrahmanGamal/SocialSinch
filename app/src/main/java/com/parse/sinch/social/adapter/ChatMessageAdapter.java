package com.parse.sinch.social.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.social.backendless.PublishSubscribeHandler;
import com.social.backendless.bus.RxOutgoingMessageBus;
import com.social.backendless.model.ChatMessage;
import com.social.backendless.model.ChatStatus;
import com.parse.sinch.social.R;
import com.parse.sinch.social.databinding.IncomingChatMessageBinding;
import com.parse.sinch.social.databinding.OutgoingChatMessageBinding;
import com.parse.sinch.social.viewmodel.ChatIncomingViewModel;
import com.parse.sinch.social.viewmodel.ChatOutgoingViewModel;

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
    private PublishSubscribeHandler mPublisherSubHandler;

    private static final String TAG = "ChatMessageAdapter";

    private static final int MESSAGE_OUTGOING = 1;
    private static final int MESSAGE_INCOMING = 2;

    public ChatMessageAdapter(Context context,
                              String senderId,
                              String recipientId,
                              NewItemInserted itemInsertedListener) {
        this.mContext = context;
        this.mMessages = new ArrayList<>();
        this.mItemInsertedListener = itemInsertedListener;
        this.mPublisherSubHandler = PublishSubscribeHandler.getInstance(context);
        this.mPublisherSubHandler.attachToChannel();
        setHasStableIds(true);
        retrieveOldMessages(senderId, recipientId);
        configureMessageBus();

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
                if (chatInfo.getStatus().equals(ChatStatus.RECEIVED) || chatInfo.getStatus().equals(ChatStatus.SENT)) {
                    Log.e(TAG, "RECEIVED MESSAGE FROM BUS!!!");
                    addMessage(chatInfo);
                } else if (!mMessages.isEmpty()) {
                    findMessagePosition(chatInfo);
                } else {
                    Log.e(TAG, "chat list EMPTY, ADDING MESSAGE");
                    addMessage(chatInfo);
                }
            }
        });
    }

    /**
     * Find the message in the current message list and modify the icon
     * @param message
     */
    private void findMessagePosition(ChatMessage message) {
        if (mMessages.contains(message)) {
            int position = mMessages.indexOf(message);
            changeStatusIcon(mMessages.get(position));
            notifyItemChanged(position);
        } else {
            //new message, add it
            addMessage(message);
        }
    }

    /**
     * Change the resource icon based on the message's status
     * @param message
     */
    private void changeStatusIcon(ChatMessage message) {
        switch (message.getStatus()) {
            case SENT:
                message.setResourceId(R.drawable.message_got_receipt_from_server);
                break;
            case DELIVERED:
                message.setResourceId(R.drawable.message_got_receipt_from_target);
                break;
            case READ:
                message.setResourceId(R.drawable.message_got_read_receipt_from_target);
                break;
            case FAILED:
                default:
                message.setResourceId(R.drawable.message_waiting);
                break;
        }
    }
    /**
     * Onece the adapter is intantiate, it has to retrieve the old messages saved in DB
     * @param senderId
     * @param recipientId
     */
    private void retrieveOldMessages(final String senderId, String recipientId) {
        List<ChatMessage> chatMessages = mPublisherSubHandler.retrieveLastMessages(senderId, recipientId, 100);
        for (ChatMessage oldChatMessage : chatMessages) {
            addMessage(oldChatMessage);
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

    /**
     * Sends a private message
     * @param recipientId
     * @param textBody
     */
    public void sendMessage(String recipientId, String textBody) {
        mPublisherSubHandler.sendPrivateMessage(recipientId, textBody);
    }

    /**
     * Add a message to the recyclerView
     * @param chatMessage
     */
    public void addMessage(final ChatMessage chatMessage) {
        changeStatusIcon(chatMessage);
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
