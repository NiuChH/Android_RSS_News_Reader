package com.java.niuchenhao;

import com.java.niuchenhao.bean.ChannelItem;
import com.java.niuchenhao.bean.FeedItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedsPresenter extends BasePresenter{
    private static Map<ChannelItem, List<FeedItem>> dataMap = new HashMap<>();
    private static Map<ChannelItem, Notifiable> adapterMap = new HashMap<>();

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

    }

    public static void addClick(ChannelItem channelItem){
        channelItem.addClickCount();
        //...

    }

    public static void notifyAdapter(ChannelItem channelItem){
        adapterMap.get(channelItem).notifyDiff();
    }
}
