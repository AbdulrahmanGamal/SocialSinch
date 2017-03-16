package com.parse.sinch.social.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.sinch.social.model.ViewMessage;
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
 * Adapter in charge of adding the views with the incoming/outgoing chat messages in the main
 * recycler view.
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.BindingHolder> {
    private Context mContext;
    private List<ViewMessage> mViewMessages;
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
        this.mViewMessages = new ArrayList<>();
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
            public void accept(ChatMessage chatMessage) throws Exception {
                Log.e(TAG, "RECEIVED MESSAGE FROM BUS!!!: " + chatMessage);
                ViewMessage viewMessage = new ViewMessage(chatMessage);
                if (chatMessage.getStatus().equals(ChatStatus.RECEIVED)) {
                    viewMessage.setViewMessageId((long) mViewMessages.size() + 1);
                    addMessage(viewMessage);
                } else if (!mViewMessages.isEmpty()) {
                    findMessagePosition(viewMessage);
                } else {
                    Log.e(TAG, "chat list EMPTY, ADDING MESSAGE");
                    addMessage(viewMessage);
                }
            }
        });
    }

    /**
     * Find the message in the current message list and modify the icon
     * @param viewMessage
     */
    private void findMessagePosition(ViewMessage viewMessage) {
        boolean found = false;
        for (int i = mViewMessages.size() -1; i > 0; i--) {
            if (mViewMessages.get(i).getChatMessage().getMessageId().equals(
                                                                        viewMessage.getChatMessage().getMessageId())) {
                changeStatusIcon(mViewMessages.get(i), viewMessage.getChatMessage().getStatus());
                notifyItemChanged(i);
                found = true;
                break;
            }
        }
        if (!found) {
            //new message, add it
            addMessage(viewMessage);
        }
    }

    /**
     * Change the resource icon based on the message's status
     * @param viewMessage
     */
    private void changeStatusIcon(ViewMessage viewMessage, ChatStatus status) {
        switch (status) {
            case SENT:
                viewMessage.setResourceId(R.drawable.message_got_receipt_from_server);
                break;
            case DELIVERED:
                viewMessage.setResourceId(R.drawable.message_got_receipt_from_target);
                break;
            case READ:
                viewMessage.setResourceId(R.drawable.message_got_read_receipt_from_target);
                break;
            case WAITING:
            case FAILED:
                default:
                viewMessage.setResourceId(R.drawable.message_waiting);
                break;
        }
    }
    /**
     * Once the adapter is instantiate, it has to retrieve the old messages saved in DB
     * @param senderId
     * @param recipientId
     */
    private void retrieveOldMessages(final String senderId, String recipientId) {
        List<ChatMessage> chatMessages = mPublisherSubHandler.retrieveLastMessages(senderId, recipientId, 100);
        for (ChatMessage oldChatMessage : chatMessages) {
            ViewMessage viewMessage = new ViewMessage(oldChatMessage);
            changeStatusIcon(viewMessage, oldChatMessage.getStatus());
            viewMessage.setViewMessageId((long) mViewMessages.size() + 1);
            addMessage(viewMessage);
        }
    }
    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MESSAGE_INCOMING) {
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
            return MESSAGE_INCOMING;
        }
        return MESSAGE_OUTGOING;
    }
    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        if (holder instanceof IncomingBindingHolder) {
            IncomingChatMessageBinding binding =
                    ((IncomingBindingHolder) holder).getIncomingChatMessageBinding();
            binding.setViewModel(new ChatIncomingViewModel(mContext, mViewMessages.get(position)));
        } else {
            OutgoingChatMessageBinding binding =
                    ((OutgoingBindingHolder) holder).getOutgoingChatMessageBinding();
            binding.setViewModel(new ChatOutgoingViewModel(mViewMessages.get(position)));
        }
    }

    /**
     * Sends a private message
     * @param recipientId
     * @param textBody
     */
    public void sendMessage(String recipientId, String textBody) {
        ChatMessage chatMessage = new ChatMessage(recipientId, textBody);
        ViewMessage viewMessageToSend = new ViewMessage(chatMessage);
        viewMessageToSend.setViewMessageId((long) mViewMessages.size() + 1);
        changeStatusIcon(viewMessageToSend, ChatStatus.SEND);
        addMessage(viewMessageToSend);
        mPublisherSubHandler.sendPrivateMessage(chatMessage);
    }

    /**
     * Add a message to the recyclerView
     * @param viewChatMessage
     */
    public void addMessage(final ViewMessage viewChatMessage) {
        mViewMessages.add(viewChatMessage);
        notifyItemInserted(mViewMessages.size());
        //notify the recycler view to scroll up the recycler view
        mItemInsertedListener.onItemInserted();
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
