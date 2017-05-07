package com.social.valgoodchat.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.social.backendless.utils.LoggedUser;
import com.social.backendless.bus.RxIncomingMessageBus;
import com.social.backendless.bus.RxOutgoingMessageBus;
import com.social.backendless.database.ChatBriteDataSource;
import com.social.backendless.model.ChatMessage;
import com.social.backendless.model.ChatStatus;
import com.social.valgoodchat.R;
import com.social.valgoodchat.databinding.IncomingChatMessageBinding;
import com.social.valgoodchat.databinding.OutgoingChatMessageBinding;
import com.social.valgoodchat.model.ViewMessage;
import com.social.valgoodchat.utils.Constants;
import com.social.valgoodchat.utils.Utils;
import com.social.valgoodchat.viewmodel.ChatIncomingViewModel;
import com.social.valgoodchat.viewmodel.ChatOutgoingViewModel;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Adapter in charge of adding the views with the incoming/outgoing chat messages in the main
 * recycler view.
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.BindingHolder> {
    private List<ViewMessage> mViewMessages;
    private NewItemInserted mItemInsertedListener;
    private ChatBriteDataSource mDataSource;
    private int mLastMessageType;

    private static final String TAG = "ChatMessageAdapter";

    public ChatMessageAdapter(Context context,
                              String recipientId,
                              NewItemInserted itemInsertedListener) {
        this.mViewMessages = new ArrayList<>();
        this.mItemInsertedListener = itemInsertedListener;
        this.mDataSource = ChatBriteDataSource.getInstance(context);
        this.mLastMessageType = -1;
        setHasStableIds(true);
        retrieveOldMessages(recipientId);
        configureMessageBus();

    }

    /**
     * Attach this class with the bus so it can receive notification about the
     * messages sent and received
     */
    private void configureMessageBus() {
        RxOutgoingMessageBus.getInstance().getMessageObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(chatMessage -> {
                    Log.e(TAG, "RECEIVED MESSAGE FROM BUS!!!: " + chatMessage);
                    ViewMessage viewMessage = new ViewMessage(chatMessage);
                    if (chatMessage.getStatus().equals(ChatStatus.RECEIVED)) {
                        processReceivedMessage(viewMessage);
                    } else {
                        findMessagePosition(viewMessage);
                    }
                });
    }

    /**
     * Assign a secuential id based on the messages size and add it to the RV
     * @param viewMessage
     */
    private void processReceivedMessage(ViewMessage viewMessage) {
        viewMessage.setViewMessageId((long) mViewMessages.size() + 1);
        addMessage(viewMessage);
    }
    /**
     * Find the message in the current message list and modify the icon
     * @param viewMessage
     */
    private void findMessagePosition(ViewMessage viewMessage) {
        boolean found = false;
        for (int i = mViewMessages.size() -1; i >= 0; i--) {
            if (mViewMessages.get(i).getChatMessage().getMessageId().equals(
                                                                        viewMessage.getChatMessage().getMessageId())) {
                Utils.changeStatusIcon(mViewMessages.get(i), viewMessage.getChatMessage().getStatus());
                notifyItemChanged(i);
                found = true;
                break;
            }
        }
        if (!found) {
            //new message, add it
            if (viewMessage.getViewMessageId() == null) {
                viewMessage.setViewMessageId((long)mViewMessages.size() + 1);
            }
            addMessage(viewMessage);
        }
    }
    /**
     * Once the adapter is instantiate, it has to retrieve the old messages saved in DB
     * @param recipientId
     */
    private void retrieveOldMessages(String recipientId) {
        List<ChatMessage> chatMessages =
                mDataSource.retrieveLastMessages(LoggedUser.getInstance().getUserIdLogged(),
                                                                                recipientId, 100);
        for (ChatMessage oldChatMessage : chatMessages) {
            ViewMessage viewMessage = new ViewMessage(oldChatMessage);
            Utils.changeStatusIcon(viewMessage, oldChatMessage.getStatus());
            viewMessage.setViewMessageId((long) mViewMessages.size() + 1);
            addMessage(viewMessage);
        }
    }
    /**
     * Sends a private message
     * @param recipientId
     * @param textBody
     */
    public void sendMessage(String recipientId, String textBody) {
        ChatMessage chatMessage = new ChatMessage(recipientId, textBody);
        chatMessage.setStatus(ChatStatus.WAITING);
        ViewMessage viewMessageToSend = new ViewMessage(chatMessage);
        viewMessageToSend.setViewMessageId((long) mViewMessages.size() + 1);
        Utils.changeStatusIcon(viewMessageToSend, chatMessage.getStatus());
        addMessage(viewMessageToSend);
        Log.d(TAG, "Sending Message written to RxIncomingMessageBus");
        RxIncomingMessageBus.getInstance().sendMessage(chatMessage);
    }

    /**
     * Add a message to the recyclerView
     * @param viewChatMessage
     */
    public void addMessage(final ViewMessage viewChatMessage) {
        if (viewChatMessage.getChatMessage().getStatus().equals(ChatStatus.RECEIVED)) {
            viewChatMessage.setFirstMessage(mLastMessageType == Constants.MESSAGE_INCOMING);
            mLastMessageType = Constants.MESSAGE_INCOMING;
        } else {
            viewChatMessage.setFirstMessage(mLastMessageType == Constants.MESSAGE_OUTGOING);
            mLastMessageType = Constants.MESSAGE_OUTGOING;
        }
        mViewMessages.add(viewChatMessage);

        notifyItemInserted(mViewMessages.size());
        //notify the recycler view to scroll up the recycler view
        mItemInsertedListener.onItemInserted();
    }
    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.MESSAGE_INCOMING) {
            ViewDataBinding incomingChatMessageBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.incoming_chat_message, parent, false);
            return new IncomingBindingHolder((IncomingChatMessageBinding) incomingChatMessageBinding);
        } else {
            ViewDataBinding outgoingChatMessageBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.outgoing_chat_message, parent, false);
            return new OutgoingBindingHolder((OutgoingChatMessageBinding) outgoingChatMessageBinding);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mViewMessages.get(position).getChatMessage().getStatus().equals(ChatStatus.RECEIVED)) {
            return Constants.MESSAGE_INCOMING;
        }
        return Constants.MESSAGE_OUTGOING;
    }
    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        if (holder instanceof IncomingBindingHolder) {
            IncomingChatMessageBinding binding =
                    ((IncomingBindingHolder) holder).getIncomingChatMessageBinding();
            binding.setViewModel(new ChatIncomingViewModel(mViewMessages.get(position)));
        } else {
            OutgoingChatMessageBinding binding =
                    ((OutgoingBindingHolder) holder).getOutgoingChatMessageBinding();
            binding.setViewModel(new ChatOutgoingViewModel(mViewMessages.get(position)));
        }
    }


    @Override
    public int getItemCount() {
        return mViewMessages.size();
    }

    @Override
    public long getItemId(int position) {
        return mViewMessages.get(position).getViewMessageId();
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
