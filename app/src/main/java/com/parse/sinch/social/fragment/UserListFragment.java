package com.parse.sinch.social.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.sinch.social.R;
import com.parse.sinch.social.databinding.FragmentCallsListBinding;
import com.parse.sinch.social.viewmodel.ListUserCallViewModel;

/**
 * Created by GeorgeLocal on 29/12/2014.
 * <p>
 * Fragment used in Tab Users
 */
public class UserListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        final View rootView = inflater.inflate(R.layout.fragment_calls_list, container, false);
        FragmentCallsListBinding fragmentCallsListBinding = FragmentCallsListBinding.bind(rootView);
        fragmentCallsListBinding.setViewModel(new ListUserCallViewModel(getContext()));
        return rootView;
    }
}
