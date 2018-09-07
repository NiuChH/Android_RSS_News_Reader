package com.java.niuchenhao;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;

import com.java.niuchenhao.utils.OpmlReader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChannelsPresenter {

    private static ChannelsPresenter channelsPresenter = null;
    private static ChannelPagerAdapter channelPagerAdapter = null;
    private static List<ChannelItem> channelItemArrayList;
    private static List<ChannelItem> checkedChannels;
    private static List<ChannelItem> uncheckedChannels;
    private static List<ChannelAdapter> adapterList = new LinkedList<>();

    private static ChannelItem recommendChannelItem =
            new ChannelItem("推荐", "http://www.people.com.cn/rss/game.xml");

    private ChannelsPresenter(Context context){
        channelItemArrayList = OpmlReader.readData(context, "opml.xml");
        checkedChannels = new ArrayList<>(channelItemArrayList.subList(0, 3));
        uncheckedChannels = new ArrayList<>(channelItemArrayList.subList(3, channelItemArrayList.size()));
    }

    public static void registerAdapter(Object adapter){
        if(adapter instanceof ChannelAdapter)
            adapterList.add((ChannelAdapter)adapter);
        else if(adapter instanceof ChannelPagerAdapter)
            channelPagerAdapter = (ChannelPagerAdapter) adapter;
    }

    public static ChannelsPresenter getChannelsPresenter(Context context){
        if(channelsPresenter==null){
            channelsPresenter = new ChannelsPresenter(context);
        }
        return channelsPresenter;
    }
    public static ChannelsPresenter getChannelsPresenter(){
        return channelsPresenter;
    }

    public static List<ChannelItem> getCheckedChannels() {
        return checkedChannels;
    }

    public static List<ChannelItem> getUncheckedChannels() {
        return uncheckedChannels;
    }

    public static ChannelItem getRecommendChannelItem(){
        return recommendChannelItem;
    }

    public static boolean toggleCheck(ChannelItem item){
        for(ChannelItem i: checkedChannels){
            if(i.getId().equals(item.getId())) {
                checkedChannels.remove(i);
                uncheckedChannels.add(i);
                updateAdapter();
                return true;
            }
        }
        for(ChannelItem i: uncheckedChannels){
            if(i.getId().equals(item.getId())) {
                checkedChannels.add(i);
                uncheckedChannels.remove(i);
                updateAdapter();
                return true;
            }
        }
        return false;
    }


    private static void updateAdapter(){
        for(ChannelAdapter adapter : adapterList)
            if(adapter != null)
                adapter.notifyDiff();
        if(channelPagerAdapter != null)
            channelPagerAdapter.notifyDataSetChanged();
    }
}