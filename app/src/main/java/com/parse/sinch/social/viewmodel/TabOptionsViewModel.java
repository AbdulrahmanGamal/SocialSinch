package com.parse.sinch.social.viewmodel;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.parse.sinch.social.adapter.TabOptionsPagerAdapter;
import com.parse.sinch.social.fragment.UserListFragment;

/**
 * Created by valgood on 2/19/2017.
 */

public class TabOptionsViewModel {
    private Context mContext;
    private TabOptionsPagerAdapter mUserChatCollectionPagerAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public TabOptionsViewModel(Context context, FragmentManager fragmentManager) {
        this.mContext = context;
        this.mUserChatCollectionPagerAdapter = new TabOptionsPagerAdapter(fragmentManager);
    }

    @BindingAdapter("tabOptionsViewModel")
    public static void setTabOptionsViewModel(ViewPager viewPager,
                                            TabOptionsViewModel viewModel) {
        viewModel.setViewPager(viewPager);
    }
    public void setupWithViewPager() {
        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        //mUserChatCollectionPagerAdapter.addFragment(new UserListFragment(), "CALLS");
        mUserChatCollectionPagerAdapter.addFragment(new UserListFragment(), "CHATS");
        //mUserChatCollectionPagerAdapter.addFragment(new UserListFragment(), "CONTACTS");
        mViewPager.setAdapter(mUserChatCollectionPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @BindingAdapter("tabLayoutViewModel")
    public static void setupTabLayout(TabLayout tabLayout, TabOptionsViewModel viewModel) {
        viewModel.setTabLayout(tabLayout);
    }

    private void setTabLayout(TabLayout tabLayout) {
        this.mTabLayout = tabLayout;
    }

    private void setViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;
    }
}