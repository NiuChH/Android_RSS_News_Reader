package com.java.niuchenhao.utils;

import android.content.Context;
import android.util.Log;

import com.java.niuchenhao.ChannelItem;
import com.java.niuchenhao.FeedItem;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// TODO move it to server
public class RssReader {

    private RssReader(){
        throw new AssertionError("RssReader is a static class");
    }

//    private static ArrayList<FeedItem> feedItems = new ArrayList<>();

    public static List<FeedItem> readData(Context context, ChannelItem channelItem){
        List<FeedItem> feedItems = new ArrayList<>();
        try {
            Document doc = Jsoup.parse(new URL(channelItem.getXmlUrl()), 6000);
            if (doc != null) {
                Elements items = doc.getElementsByTag("item");
                for(Element item: items){
//                Log.d("item", item.tagName()+" "+item.text());
                    FeedItem feeditem = new FeedItem();
                    feeditem.setChannelId(channelItem.getId());
                    Elements eles = item.children();
                    for(Element e : eles){
//                    Log.d("ele", e.tagName()+" "+e.text());
                        switch (e.tagName()){
                            case "title":
                                feeditem.setTitle(e.text());
                                break;
                            case "description":
                                feeditem.setDescription(e.text().replaceAll("<img.*?/>", ""));
                                Elements maybeImg = Jsoup.parse(e.text()).getElementsByTag("img");
                                if(!maybeImg.isEmpty())
                                    feeditem.setThumbnailUrl(maybeImg.first().attr("src"));
                                else
                                    // TODO edit default ThumbnailUrl
                                    feeditem.setThumbnailUrl("drawable/nav_icon.png");
                                break;
                            case "pubDate":
                                feeditem.setPubDate(e.text());
                                break;
                            case "link":
                                feeditem.setLink(e.text());
//                            Log.d("ele", e.tagName()+" "+e.text());
                                break;
                        }
                    }
                    // TODO add more strict roles here
                    if(feeditem.getDescription().length() >= 20)
                        feedItems.add(feeditem);
                }
            } else {
                Log.e("RssReader", "get empty data");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return feedItems;
    }

}
