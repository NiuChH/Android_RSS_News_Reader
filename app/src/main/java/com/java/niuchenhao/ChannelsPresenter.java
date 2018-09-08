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
            // just a example
            new ChannelItem("推荐", "http://www.people.com.cn/rss/game.xml");

    private ChannelsPresenter(){}

    public static void initChannelsPresenter(Context context){

        channelItemArrayList = DatabaseModel.getChannelsSync(context);

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
            if(i.getXmlUrl().equals(item.getXmlUrl())) {
                checkedChannels.remove(i);
                uncheckedChannels.add(i);
                notifyAdapter();
                return;
            }
        }
        for(ChannelItem i: uncheckedChannels){
            if(i.getXmlUrl().equals(item.getXmlUrl())) {
                checkedChannels.add(i);
                uncheckedChannels.remove(i);
                notifyAdapter();
                return;
            }
        }
    }
}
