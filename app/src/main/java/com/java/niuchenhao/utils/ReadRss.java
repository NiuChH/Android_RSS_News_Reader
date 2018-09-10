package com.java.niuchenhao.utils;

import android.os.AsyncTask;

import android.app.ProgressDialog;
import android.support.annotation.Nullable;
import android.util.Log;

import com.java.niuchenhao.model.DatabaseModel;
import com.java.niuchenhao.model.bean.ChannelItem;
import com.java.niuchenhao.model.bean.FeedItem;
import com.java.niuchenhao.presenter.FeedsPresenter;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import org.jsoup.*;
import org.litepal.LitePal;

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
        if (!success)
            DatabaseModel.setIsOnline(false);
        FeedsPresenter.notifyAdapter(channelItem);
    }

    // In this method we will process Rss feed  document we downloaded to parse useful information from it

    private Boolean ProcessXml(Document data, Integer numbers, Integer mode) {
        FeedItem feedItemFromDB;
        if (data != null) {
            temp_feedItems = new ArrayList<>();
            Elements items = data.getElementsByTag("item");
            for (int i = (mode.equals(APPEND) ? feedItems.size() : 0); i < items.size(); ++i) {
//                Log.d("item", item.tagName()+" "+item.text());
                Element item = items.get(i);
                FeedItem feeditem = new FeedItem();
                feeditem.setChannelTitle(channelItem.getTitle());
                Elements eles = item.children();
                //                    Log.d("ele", e.tagName()+" "+e.text());
                for (Element e : eles)
                    switch (e.tagName()) {
                        case "title":
                            feeditem.setTitle(e.text());
                            break;
                        case "full-text":
                        case "description":
                            feeditem.setDescription(e.text().replaceAll("(<img.*?/>)|(<IMG.*?/>)", ""));
                            Elements maybeImg = Jsoup.parse(e.text()).getElementsByTag("img");
                            maybeImg.addAll(Jsoup.parse(e.text()).getElementsByTag("IMG"));
                            if (!maybeImg.isEmpty()) {
                                for (Element img : maybeImg) {
                                    if (img.attr("src").startsWith("http://")) {
                                        feeditem.setThumbnailUrl(img.attr("src"));
                                        break;
                                    }else if (img.attr("src").startsWith("/")) {
                                        feeditem.setThumbnailUrl("http://www.people.com.cn"+img.attr("src"));
                                        break;
                                    } else {
                                        Log.d("ReadRss error image", img.attr("src"));
                                    }
                                }
                            } else
                                feeditem.setThumbnailUrl(null);
                            break;
                        case "pubDate":
                            feeditem.setPubDate(e.text());
                            break;
                        case "link":
                            feeditem.setLink(e.text());
                            break;
                    }
                feedItemFromDB = LitePal.where("link = ?", feeditem.getLink()).findFirst(FeedItem.class);
                if (feedItemFromDB != null)
                    temp_feedItems.add(feedItemFromDB);
                else if (feeditem.getDescription().length() >= 100) {
                    temp_feedItems.add(feeditem);
                    feeditem.saveAsync();
                }
                if (temp_feedItems.size() > numbers)
                    break;
            }
            if (mode.equals(REFRESH)) {
                feedItems.clear();
                feedItems.addAll(temp_feedItems);
            } else {
                feedItems.addAll(temp_feedItems);
            }
        } else {
            Log.e("ReadRss", "get empty data");
            return Boolean.FALSE;
        }
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