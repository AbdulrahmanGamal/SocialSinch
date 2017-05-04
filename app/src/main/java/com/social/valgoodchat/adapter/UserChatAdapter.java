package com.social.valgoodchat.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.social.backendless.bus.RxIncomingEventBus;
import com.social.backendless.bus.RxOutgoingMessageBus;
import com.social.backendless.database.ChatBriteDataSource;
import com.social.backendless.model.ChatMessage;
import com.social.backendless.model.EventMessage;
import com.social.backendless.model.EventStatus;
import com.social.backendless.model.UserInfo;
import com.social.backendless.utils.DateUtils;
import com.social.backendless.utils.LoggedUser;
import com.social.valgoodchat.R;
import com.social.valgoodchat.databinding.UserChatInfoBinding;
import com.social.valgoodchat.model.UserViewInfoMessage;
import com.social.valgoodchat.model.ViewMessage;
import com.social.valgoodchat.viewmodel.UserCallsItemViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class UserChatAdapter extends RecyclerView.Adapter<UserChatAdapter.BindingHolder> {

	private Context mContext;
	private List<UserViewInfoMessage> mUserChats;
    private ChatBriteDataSource mDataSource;

	private static final String TAG = "UserCallsAdapter";
	
	public UserChatAdapter(Context context){
		this.mContext = context;
		this.mUserChats = new ArrayList<>();
        this.mDataSource = ChatBriteDataSource.getInstance(context);
        configureMessageBus();
	}
	
    @Override
	public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserChatInfoBinding userChatBinding = DataBindingUtil.inflate(
                         LayoutInflater.from(parent.getContext()),
                         R.layout.user_chat_info, parent, false);
		return new BindingHolder(userChatBinding);
	}

	@Override
	public void onBindViewHolder(BindingHolder holder, int position) {
        UserChatInfoBinding userChatBinding = holder.binding;
        userChatBinding.setViewModel(new UserCallsItemViewModel(mContext, mUserChats.get(position)));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return mUserChats.size();
	}

    public void setUserCalls(List<UserInfo> userChats) {
        for (UserInfo userInfo : userChats) {
            ChatMessage lastChatMessage =
                    mDataSource.retrieveLastMessage(LoggedUser.getInstance().getUserIdLogged(),
                            userInfo.getObjectId());
            UserViewInfoMessage userVM =
                    new UserViewInfoMessage(userInfo, new ViewMessage(lastChatMessage));
            mUserChats.add(userVM);
			notifyOnlineStatus(userInfo);
        }
        notifyDataSetChanged();
    }
    public void refreshLastMessage() {
		for (UserViewInfoMessage userVM : mUserChats) {
			ChatMessage lastChatMessage =
					mDataSource.retrieveLastMessage(LoggedUser.getInstance().getUserIdLogged(),
							userVM.getUserInfo().getObjectId());
			userVM.updateLastMessage(lastChatMessage);
		}
		notifyDataSetChanged();
	}
	/**
	 * Only Notify users that are currently Online
	 */
	private void notifyOnlineStatus(UserInfo userInfo) {
        if (userInfo.isOnline()) {
                EventMessage eventMessage = new EventMessage(LoggedUser.getInstance().getUserIdLogged(),
                        userInfo.getObjectId(),
                        EventStatus.ONLINE.toString(),
                        EventStatus.ONLINE);
                RxIncomingEventBus.getInstance().sendEvent(eventMessage);
		}
	}

	public class BindingHolder extends RecyclerView.ViewHolder {
        private UserChatInfoBinding binding;

        BindingHolder(UserChatInfoBinding binding) {
			super(binding.userElement);
            this.binding = binding;
		}
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
					processIncomingMessage(chatMessage);
					notifyDataSetChanged();
                });
	}
    /**
     * Refresh the last message information shown under the user name
     * @param message
     */
	private void processIncomingMessage(ChatMessage message) {
        for (UserViewInfoMessage userViewInfoMessage: mUserChats) {
            if (userViewInfoMessage.getUserInfo().getObjectId().equals(message.getSenderId())) {
                userViewInfoMessage.setLastViewMessage(new ViewMessage(message));
                break;
            }
        }
    }
}
