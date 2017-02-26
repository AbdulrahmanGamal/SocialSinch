package com.parse.sinch.social.viewmodel;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.parse.sinch.social.model.ChatMessage;

/**
 * Created by valgood on 2/20/2017.
 */

public class ChatOutgoingViewModel {
    private ChatMessage mChatMessage;

    public ChatOutgoingViewModel(final ChatMessage chatMessage){
        this.mChatMessage = chatMessage;

//        RxOutgoingMessageBus.getInstance().getMessageObservable().subscribe(new Consumer<ChatInfo>() {
//            @Override
//            public void accept(ChatInfo chatInfo) throws Exception {
//                if (chatInfo.getStatus().equals(ChatInfo.ChatStatus.DELIVERED) &&
//                    chatInfo.getMessageId().equals(mChatMessage.getMessageId())) {
//                    mChatMessage.setStatus(chatInfo.getStatus());
//                    mChatMessage.setTextBody("DELIVERED");
//                    mIconStatus = ContextCompat.getDrawable(mContext, R.drawable.message_got_read_receipt_from_target);
//                } else if (chatInfo.getSenderId() != null && chatInfo.getSenderId().equals(mChatMessage.getSenderId())
//                    && chatInfo.getTextBody().equals(mChatMessage.getTextBody())) {
//                    switch (mChatMessage.getStatus()) {
//                        case WAITING:
//                            mChatMessage.setMessageId(chatInfo.getMessageId());
//                            mChatMessage.setRecipientIds(chatInfo.getRecipientIds());
//                            mChatMessage.setStatus(chatInfo.getStatus());
//                            mChatMessage.setTimestamp(chatInfo.getTimestamp());
//                            mChatMessage.setTextBody("SENT");
//                            mIconStatus = ContextCompat.getDrawable(mContext, R.drawable.message_got_receipt_from_target);
//                            break;
//                    }
//                }
//
//                }
//        });
    }

//    public void removeMessageClientListener() {
//        mConnection.removeMessageClientListener(mMessageClientListener);
//    }
    @BindingAdapter("android:src")
    public static void setImageResource(ImageView indicatorImgView, ChatOutgoingViewModel viewModel) {
        indicatorImgView.setImageResource(viewModel.getIconStatus());
    }
    public int getIconStatus() {
        return mChatMessage.getResourceId();
    }

    public ChatMessage getChatMessage() {
        return mChatMessage;
    }
}
