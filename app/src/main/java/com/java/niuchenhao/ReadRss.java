package com.java.niuchenhao;

import android.os.AsyncTask;

import android.app.ProgressDialog;
import android.support.annotation.Nullable;
import android.util.Log;

import com.java.niuchenhao.bean.ChannelItem;
import com.java.niuchenhao.bean.FeedItem;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import org.jsoup.*;
import org.litepal.LitePal;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by rishabh on 31-01-2016.
 */
public class ReadRss extends AsyncTask<Integer, Void, Boolean> {
    //    static private String address = "http://www.people.com.cn/rss/game.xml";
    private ProgressDialog progressDialog;

    static boolean firstTime = true;
    public static final Integer APPEND = 1;
    public static final Integer REFRESH = 0;

//    private static SimpleDateFormat englishDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
//    private static SimpleDateFormat stdDateFormat = new SimpleDateFormat("YYYY-MM-dd", Locale.CHINA);

    private List<FeedItem> feedItems;
    private List<FeedItem> temp_feedItems;
    private ChannelItem channelItem;

    public ReadRss(ChannelItem channelItem,
                   List<FeedItem> feedItems) {
        this.channelItem = channelItem;
        this.feedItems = feedItems;
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("Loading...");
    }

    //before fetching of rss statrs show progress to user
    @Override
    protected void onPreExecute() {
//        if(firstTime)
//            progressDialog.show();
        super.onPreExecute();
    }


    //This method will execute in background so in this method download rss feeds
    @Override
    protected Boolean doInBackground(Integer... args) {
        //call process xml method to process document we downloaded from getData() method
        return ProcessXml(Getdata(), args[0], args[1]);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
//        if(firstTime) {
//            progressDialog.dismiss();
//            firstTime = false;
//        }
        if(!success)
            DatabaseModel.setIsOnline(false);
        FeedsPresenter.notifyAdapter(channelItem);
    }

    // In this method we will process Rss feed  document we downloaded to parse useful information from it

    private Boolean ProcessXml(Document data, Integer numbers, Integer mode) {
        if (data != null) {
            temp_feedItems = new ArrayList<>();
            Elements items = data.getElementsByTag("item");
            for(int i = (mode.equals(APPEND) ? feedItems.size() : 0); i < items.size(); ++i){
//                Log.d("item", item.tagName()+" "+item.text());
                Element item = items.get(i);
                FeedItem feeditem = new FeedItem();
                feeditem.setChannelTitle(channelItem.getTitle());
                Elements eles = item.children();
                for(Element e : eles){
//                    Log.d("ele", e.tagName()+" "+e.text());
                    switch (e.tagName()){
                        case "title":
                            feeditem.setTitle(e.text());
                            break;
                        case "full-text":
                        case "description":
                            feeditem.setDescription(e.text().replaceAll("<img.*?/>", ""));
                            Elements maybeImg = Jsoup.parse(e.text()).getElementsByTag("img");
                            if(!maybeImg.isEmpty())
                                feeditem.setThumbnailUrl(maybeImg.first().attr("src"));
                            else
                                // TODO edit default ThumbnailUrl
                                feeditem.setThumbnailUrl("file:///android_asset/rss_loge.gif");
                            break;
                        case "pubDate":
                                feeditem.setPubDate(e.text());
                            break;
                        case "link":
                            feeditem.setLink(e.text());
                            break;
                    }
                }
                if(feeditem.getDescription().length() >= 20)
                    temp_feedItems.add(feeditem);
                if(temp_feedItems.size() > numbers)
                    break;
            }
            if(mode.equals(REFRESH)) {
                feedItems.clear();
                feedItems.addAll(temp_feedItems);
            } else {
                feedItems.addAll(temp_feedItems);
            }
        } else {
            Log.e("ReadRss", "get empty data");
            return Boolean.FALSE;
        }
        for (FeedItem feedItem : temp_feedItems)
            try {
            //TODO optimize this!
                feedItem.save();
            } catch (Exception ignored){}
        return Boolean.TRUE;
    }

    //This method will download rss feed document from specified url
    @Nullable
    private Document Getdata() {
        Log.d("ReadRSS", "GetData");
        try {
            Document doc = Jsoup.parse(new URL(channelItem.getXmlUrl()), 4000);
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}