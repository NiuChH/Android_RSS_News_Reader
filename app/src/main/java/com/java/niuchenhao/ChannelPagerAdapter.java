package com.java.niuchenhao;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ChannelPagerAdapter extends FragmentPagerAdapter {

    private Resources resources;

    public ChannelPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        resources = context.getResources();
    }

    private ChannelItem getChannelItem(int position){
        if(position == 0)
            return ChannelsPresenter.getRecommendChannelItem();
        return ChannelsPresenter.getCheckedChannels().get(position-1);
    }

    /** Instantiate fragment based on user horizontal scroll position */
    @Override
    public Fragment getItem(int position) {
        return NewsFragment.newInstance(getChannelItem(position).getXmlUrl());
    }

    /** Informs the adapter of the total number of available fragments views */
    @Override
    public int getCount() {
        return ChannelsPresenter.getCheckedChannels().size()+1;
    }

    /** Set tab title */
    @Override
    public CharSequence getPageTitle(int position) {
        return getChannelItem(position).getTitle();
    }
}