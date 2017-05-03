package com.social.valgoodchat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.social.backendless.utils.DateUtils;
import com.social.valgoodchat.R;
import com.social.valgoodchat.databinding.FragmentCallsListBinding;
import com.social.valgoodchat.viewmodel.ListUserChatViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by GeorgeLocal on 29/12/2014.
 * <p>
 * Fragment used in Tab Users
 */
public class UserListFragment extends Fragment {

    private ListUserChatViewModel mListUserCallViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        final View rootView = inflater.inflate(R.layout.fragment_calls_list, container, false);
        FragmentCallsListBinding fragmentCallsListBinding = FragmentCallsListBinding.bind(rootView);
        mListUserCallViewModel = new ListUserChatViewModel(getContext());
        fragmentCallsListBinding.setViewModel(mListUserCallViewModel);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Date currentTime = Calendar.getInstance(Locale.getDefault()).getTime();
        mListUserCallViewModel.
                notifyConnectionStatus(DateUtils.convertDateToString(currentTime));
    }

    @Override
    public void onResume() {
        super.onResume();
        mListUserCallViewModel.refreshLastMessage();
    }
}
