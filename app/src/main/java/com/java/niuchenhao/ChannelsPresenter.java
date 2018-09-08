package com.java.niuchenhao;

import android.content.Context;

import com.java.niuchenhao.bean.ChannelItem;
import com.java.niuchenhao.utils.OpmlReader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChannelsPresenter extends BasePresenter {

    private static BasePresenter mPresenter = new ChannelsPresenter();
    private static List<ChannelItem> channelItemArrayList;
    private static List<ChannelItem> checkedChannels;
    private static List<ChannelItem> uncheckedChannels;

    private static ChannelItem recommendChannelItem =
            // TODO implement Recommendation
            new ChannelItem("推荐", "http://www.people.com.cn/rss/game.xml");

    private ChannelsPresenter(){

        // TODO get form db(sync)
        //channelItemArrayList = OpmlReader.readData(context, "opml.xml");

        checkedChannels = new ArrayList<>(channelItemArrayList.subList(0, 3));
        uncheckedChannels = new ArrayList<>(channelItemArrayList.subList(3, channelItemArrayList.size()));
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

    public static void toggleCheck(ChannelItem item){
        for(ChannelItem i: checkedChannels){
            if(i.getId().equals(item.getId())) {
                checkedChannels.remove(i);
                uncheckedChannels.add(i);
                notifyAdapter();
                return;
            }
        }
        for(ChannelItem i: uncheckedChannels){
            if(i.getId().equals(item.getId())) {
                checkedChannels.add(i);
                uncheckedChannels.remove(i);
                notifyAdapter();
                return;
            }
        }
    }
}
