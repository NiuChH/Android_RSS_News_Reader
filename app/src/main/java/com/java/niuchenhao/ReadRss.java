package com.java.niuchenhao;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.jsoup.*;

/**
 * Created by rishabh on 31-01-2016.
 */
public class ReadRss extends AsyncTask<String, Void, Void> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
//    static private String address = "http://www.people.com.cn/rss/game.xml";
    private ProgressDialog progressDialog;

    private FeedsAdapter adapter;
    private URL url;
    private SwipeRefreshLayout swipeRefreshLayout;
    static boolean firstTime = true;
    private List<FeedItem> feedItems;
    private List<FeedItem> temp_feedItems;

    public ReadRss(Context context,
                   FeedsAdapter adapter,
                   SwipeRefreshLayout swipeRefreshLayout,
                   List<FeedItem> feedItems) {
        this.context = context;
        this.adapter = adapter;
        this.feedItems = feedItems;
        this.swipeRefreshLayout = swipeRefreshLayout;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
    }

    //before fetching of rss statrs show progress to user
    @Override
    protected void onPreExecute() {
        if(firstTime)
            progressDialog.show();
        super.onPreExecute();
    }


    //This method will execute in background so in this method download rss feeds
    @Override
    protected Void doInBackground(String... addresses) {
        //call process xml method to process document we downloaded from getData() method
        Log.d("addresses", Arrays.toString(addresses));
        ProcessXml(Getdata(addresses[0]));
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(firstTime) {
            progressDialog.dismiss();
            firstTime = false;
        }
        adapter.notifyDiff();
        swipeRefreshLayout.setRefreshing(false);
    }

    // In this method we will process Rss feed  document we downloaded to parse useful information from it

    private void ProcessXml(Document data) {
        if (data != null) {
            temp_feedItems = new ArrayList<>();
            Elements items = data.getElementsByTag("item");
            for(Element item: items){
//                Log.d("item", item.tagName()+" "+item.text());
                FeedItem feeditem = new FeedItem();
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
                if(feeditem.getDescription().length() >= 20)
                    temp_feedItems.add(feeditem);
            }
            feedItems.clear();
            feedItems.addAll(temp_feedItems);
        } else {
            Log.e("ReadRss", "get empty data");
        }
    }

    //This method will download rss feed document from specified url
    public Document Getdata(String address) {
        Log.d("ReadRSS", "GetData");
        try {
//            url = new URL(address);
            Document doc = Jsoup.parse(new URL(address), 6000);
            Log.d("ReadRss", doc.wholeText());
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}