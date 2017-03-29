package com.parse.sinch.social.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.parse.sinch.social.R;
import com.parse.sinch.social.databinding.UserCallsInfoBinding;
import com.social.backendless.model.UserInfo;
import com.parse.sinch.social.viewmodel.UserCallsItemViewModel;

public class UserCallsAdapter extends RecyclerView.Adapter<UserCallsAdapter.BindingHolder> {

	private Context mContext;
	private List<UserInfo> mUserCalls;
	
	public UserCallsAdapter(Context context){
		this.mContext = context;
		this.mUserCalls = new ArrayList<>();
	}
	
    @Override
	public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserCallsInfoBinding userCallsBinding = DataBindingUtil.inflate(
                         LayoutInflater.from(parent.getContext()),
                         R.layout.user_calls_info, parent, false);
		return new BindingHolder(userCallsBinding);
	}

	@Override
	public void onBindViewHolder(BindingHolder holder, int position) {
        UserCallsInfoBinding userCallsBinding = holder.binding;
        userCallsBinding.setViewModel(new UserCallsItemViewModel(mContext, mUserCalls.get(position)));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return mUserCalls.size();
	}

    public void setUserCalls(List<UserInfo> userCalls) {
        mUserCalls = userCalls;
        notifyDataSetChanged();
    }

	public class BindingHolder extends RecyclerView.ViewHolder {
        private UserCallsInfoBinding binding;

        BindingHolder(UserCallsInfoBinding binding) {
			super(binding.userElement);
            this.binding = binding;
		}
	}
	
}
