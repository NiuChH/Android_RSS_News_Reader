package com.java.niuchenhao;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.java.niuchenhao.bean.ChannelItem;
import com.java.niuchenhao.bean.FeedItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FeedsPresenter extends BasePresenter{
    private static List<FeedItem> favourites = new LinkedList<>();
    private static Map<ChannelItem, List<FeedItem>> dataMap = new HashMap<>();
    private static Map<ChannelItem, Notifiable> adapterMap = new HashMap<>();
    private static Map<ChannelItem, SwipeRefreshLayout> swipeRefreshLayoutMap = new HashMap<>();

    public static void init(){
        DatabaseModel.getFavourites(favourites);
        dataMap.put(FavouriteActivity.channelItem, favourites);
    }

    public static void setSwipeRefreshLayout(ChannelItem channelItem, SwipeRefreshLayout swipeRefreshLayout){
        swipeRefreshLayoutMap.put(channelItem, swipeRefreshLayout);
    }

    public static void registerAdapter(ChannelItem channelItem, Notifiable adapter){
        adapterMap.put(channelItem, adapter);
    }

    public static void unregisterAdapter(ChannelItem channelItem, Notifiable adapter){
        if(adapterMap.get(channelItem) == adapter)
            adapterMap.remove(channelItem);
    }

    public static List<FeedItem> getFeedItemList(ChannelItem channelItem){
        if(!dataMap.containsKey(channelItem))
            dataMap.put(channelItem, new ArrayList<FeedItem>(40));
        return dataMap.get(channelItem);
    }

    public static void queryFeedItemList(ChannelItem channelItem, String keyWord, Integer number, Boolean isAppend){
        DatabaseModel.getFeedsAsync(channelItem, keyWord, number, isAppend, dataMap.get(channelItem));
    }

    public static void addComment(FeedItem feedItem){
        DatabaseModel.updateFeedItem(feedItem);
    }

    public static void addClick(FeedItem feedItem){
        for(ChannelItem ci :dataMap.keySet()){
            if(ci.getTitle().equals(feedItem.getChannelTitle())){
                ci.addClickCount();
                DatabaseModel.updateChannelItem(ci);
            }
        }
    }

    public static void toggleFavourite(FeedItem feedItem){
        DatabaseModel.updateFeedItem(feedItem);
        notifyAdapter(FavouriteActivity.channelItem);
    }

    public static void notifyAdapter(ChannelItem channelItem){
        if(channelItem == null)
            return;
        if(swipeRefreshLayoutMap.get(channelItem) != null) {
            Log.d("FeedsPresenter", swipeRefreshLayoutMap.get(channelItem)+"swipeRefreshLayout.setRefreshing(false)");
            swipeRefreshLayoutMap.get(channelItem).setRefreshing(false);
        }
        adapterMap.get(channelItem).notifyDiff();
    }

    public static List<FeedItem> getFavourites() {
        return favourites;
    }
}
