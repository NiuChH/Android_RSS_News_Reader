package com.java.niuchenhao;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.flyco.tablayout.SlidingTabLayout;
import com.java.niuchenhao.bean.ChannelItem;

public class ChannelPagerAdapter extends FragmentPagerAdapter implements Notifiable{

    private Resources resources;
    private SlidingTabLayout slidingTabLayout;

    public ChannelPagerAdapter(Context context, FragmentManager fm, SlidingTabLayout slidingTabLayout) {
        super(fm);
        this.slidingTabLayout = slidingTabLayout;
        resources = context.getResources();
        ChannelsPresenter.registerAdapter(this);
    }

    @Override
    protected void finalize() throws Throwable {
        ChannelsPresenter.unregisterAdapter(this);
        super.finalize();
    }

    private ChannelItem getChannelItem(int position){
        if(position == 0)
            return ChannelsPresenter.getRecommendChannelItem();
        return ChannelsPresenter.getCheckedChannels().get(position-1);
    }

    /** Instantiate fragment based on user horizontal scroll position */
    @Override
    public Fragment getItem(int position) {
        return NewsListFragment.newInstance(getChannelItem(position));
    }

    /** Informs the adapter of the total number of available fragments views */
    @Override
    public int getCount() {
        return ChannelsPresenter.getCheckedChannels().size()+1;
    }

    /** Set tab title */
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0)
            return " "+resources.getString(R.string.recommend)+" ";
        return " "+getChannelItem(position).getTitle()+" ";
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        slidingTabLayout.notifyDataSetChanged();
    }

    public void notifyDiff(){
        notifyDataSetChanged();
        slidingTabLayout.setCurrentTab(0);
    }
}
