package com.parse.sinch.social.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.sinch.social.R;
import com.parse.sinch.social.databinding.ChatMessageLeftBinding;
import com.parse.sinch.social.databinding.ChatMessageRightBinding;
import com.parse.sinch.social.model.ChatInfo;
import com.parse.sinch.social.model.RxMessageBus;
import com.parse.sinch.social.viewmodel.ChatItemListViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by valgood on 2/20/2017.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.BindingHolder> {

    private Context mContext;
    private List<ChatInfo> mMessages;

    private static final int MESSAGE_SENT = 1;
    private static final int MESSAGE_RECEIVED = 2;

    public ChatMessageAdapter(Context context) {
        this.mContext = context;
        this.mMessages = new ArrayList<>();
        //attach this class with the bus so it can receive notification about the
        //messsages sent ans received
        attachMessageBus();
    }

    private void attachMessageBus() {
        RxMessageBus.getInstance().getMessageObservable().subscribe(new Consumer<ChatInfo>() {
            @Override
            public void accept(ChatInfo chatInfo) throws Exception {
                addMessage(chatInfo);
            }
        });
    }
    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MESSAGE_SENT) {
            ChatMessageRightBinding chatMessageRightBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.chat_message_right, parent, false);
            return new SentBindingHolder(chatMessageRightBinding);
        } else {
            ChatMessageLeftBinding chatMessageLeftBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.chat_message_left, parent, false);
            return new ReceivedBindingHolder(chatMessageLeftBinding);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mMessages.get(position).getStatus().equals(ChatInfo.ChatStatus.RECEIVED)) {
            return MESSAGE_RECEIVED;
        }

        return MESSAGE_SENT;
    }
    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        if (holder instanceof ReceivedBindingHolder) {
            ChatMessageLeftBinding binding =
                    ((ReceivedBindingHolder) holder).getChatMessageLeftBinding();
            binding.setViewModel(new ChatItemListViewModel(mContext, mMessages.get(position)));
        } else {
            ChatMessageRightBinding binding =
                    ((SentBindingHolder) holder).getChatMessageRightBinding();
            binding.setViewModel(new ChatItemListViewModel(mContext, mMessages.get(position)));
        }
    }

    public void addMessage(ChatInfo chatMessage) {
        mMessages.add(chatMessage);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class BindingHolder extends RecyclerView.ViewHolder {
        public BindingHolder(View itemView) {
            super(itemView);
        }
    }
    private class ReceivedBindingHolder extends BindingHolder {
        private ChatMessageLeftBinding mChatMessageLeftBinding;

        public ReceivedBindingHolder(ChatMessageLeftBinding chatMessageLeftBinding) {
            super(chatMessageLeftBinding.getRoot());
            this.mChatMessageLeftBinding = chatMessageLeftBinding;
        }

        public ChatMessageLeftBinding getChatMessageLeftBinding() {
            return mChatMessageLeftBinding;
        }
    }

    private class SentBindingHolder extends BindingHolder {
        private ChatMessageRightBinding mChatMessageRightBinding;

        public SentBindingHolder(ChatMessageRightBinding chatMessageRightBinding) {
            super(chatMessageRightBinding.getRoot());
            this.mChatMessageRightBinding = chatMessageRightBinding;
        }

        public ChatMessageRightBinding getChatMessageRightBinding() {
            return mChatMessageRightBinding;
        }
    }
}
