package com.social.valgoodchat.viewmodel;

import android.databinding.BindingAdapter;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.social.valgoodchat.adapter.TabOptionsPagerAdapter;
import com.social.valgoodchat.fragment.UserListFragment;


/**
 * View Model associated with the Tab options
 */

public class TabOptionsViewModel {
    private TabOptionsPagerAdapter mUserChatCollectionPagerAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public TabOptionsViewModel(FragmentManager fragmentManager) {
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