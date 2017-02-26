package com.parse.sinch.social.viewmodel;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.parse.sinch.social.model.ChatMessage;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;

import java.util.List;

/**
 * Created by valgood on 2/20/2017.
 */

public class ChatIncomingViewModel {
    private Context mContext;
    private ChatMessage mChatMessage;
    private Drawable mIconStatus;
 //   private SinchServiceConnection mConnection;

    public ChatIncomingViewModel(Context context, ChatMessage chatMessage){
        this.mContext = context;
        this.mChatMessage = chatMessage;
        if (chatMessage.getStatus().equals(ChatMessage.ChatStatus.WAITING)) {
//            this.mConnection = new SinchServiceConnection(context);
//            for (String recipientId : chatMessage.getRecipientIds()) {
//                mConnection.sendMessage(mMessageClientListener,recipientId, chatMessage.getTextBody());
//            }
        }

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

    private MessageClientListener mMessageClientListener = new MessageClientListener() {
        @Override
        public void onIncomingMessage(MessageClient messageClient, Message message) {

        }

        @Override
        public void onMessageSent(MessageClient messageClient, Message message, String s) {

        }

        @Override
        public void onMessageFailed(MessageClient messageClient, Message message, MessageFailureInfo messageFailureInfo) {

        }

        @Override
        public void onMessageDelivered(MessageClient messageClient, MessageDeliveryInfo messageDeliveryInfo) {

        }

        @Override
        public void onShouldSendPushData(MessageClient messageClient, Message message, List<PushPair> list) {

        }
    };

    @BindingAdapter("indicator")
    public static void setIconStatus(ImageView indicatorImgView, ChatIncomingViewModel viewModel) {
        indicatorImgView.setImageDrawable(viewModel.getIconStatus());
    }
    public Drawable getIconStatus() {
        return mIconStatus;
    }

    public ChatMessage getChatMessage() {
        return mChatMessage;
    }

}
