package com.parse.sinch.social.viewmodel;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessFault;
import com.parse.sinch.social.adapter.UserCallsAdapter;
import com.parse.sinch.social.data.DataManager;
import com.parse.sinch.social.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Class to encapsulates the logic to display the calls received/made by the user in the list
 */

public class ListUserCallViewModel {
    private Context mContext;
    private UserCallsAdapter mUserCallsAdapter;
    private boolean mShowPanel;

    private static final String TAG = "ListUserCallViewModel";

    public ListUserCallViewModel(Context context) {
        this.mContext = context;
        mUserCallsAdapter = new UserCallsAdapter(context);
    }
    private UserCallsAdapter getAdapter() {
        return mUserCallsAdapter;
    }

    private RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(mContext);
    }
    public boolean getShowPanel() {
        return mShowPanel;
    }

    public void setShowPanel(boolean showPanel) {
        this.mShowPanel = showPanel;
    }
    @BindingAdapter("userCallViewModel")
    public static void setUserCallViewModel(RecyclerView recyclerView,
                                                ListUserCallViewModel viewModel) {
        viewModel.getUserCalls(recyclerView);
        recyclerView.setAdapter(viewModel.getAdapter());
        recyclerView.setLayoutManager(viewModel.createLayoutManager());
    }
    private void getUserCalls(final RecyclerView callsRecyclerView) {
        DataManager.getFetchAllUsersObservable().doOnSubscribe(new Action0() {
            @Override
            public void call() {
                  setShowPanel(true);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        //empty implementation doesn't apply here
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error trying to get the List of Calls");
                    }

                    @Override
                    public void onNext(Object data) {
                        processResponse(callsRecyclerView, data);
                        setShowPanel(false);
                    }
                });
    }

    private void processResponse(final RecyclerView callsRecyclerView, Object data) {
        if (data instanceof BackendlessCollection) {
            BackendlessCollection<BackendlessUser> users =
                    (BackendlessCollection<BackendlessUser>) data;
            mUserCallsAdapter.setUserCalls(convertToUserInfo(users));
        } else {
            BackendlessFault fault = (BackendlessFault) data;
            //reset the previous list of calls
            mUserCallsAdapter = new UserCallsAdapter(mContext);
            showError(fault.getMessage());
        }
        callsRecyclerView.setAdapter(mUserCallsAdapter);
    }

    private void showError(String message) {
        Toast.makeText(mContext,
                "Error Loading Users: " + message,
                Toast.LENGTH_LONG).show();
    }
    private List<UserInfo> convertToUserInfo(BackendlessCollection<BackendlessUser> calls) {
        List<BackendlessUser> userList = calls.getData();
        UserInfo user;
        List<UserInfo> lstUsers = new ArrayList<>();
        for (BackendlessUser backendlessUser : userList) {
            user = new UserInfo();
            user.setObjectId(backendlessUser.getObjectId());
            user.setFullName((String) backendlessUser.getProperty("full_name"));
            user.setPhoneNumber((String) backendlessUser.getProperty("phone"));
            user.setProfilePicture((String) backendlessUser.getProperty("avatar"));
            lstUsers.add(user);
        }
        return lstUsers;
    }
}
