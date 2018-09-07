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
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
            Element root = data.getDocumentElement();
            Node channel = root.getChildNodes().item(1);
            NodeList items = channel.getChildNodes();
            for (int i = 0; i < items.getLength(); i++) {
                Node cureentchild = items.item(i);
                if (cureentchild.getNodeName().equalsIgnoreCase("item")) {
                    FeedItem item = new FeedItem();
                    NodeList itemchilds = cureentchild.getChildNodes();
                    for (int j = 0; j < itemchilds.getLength(); j++) {
                        Node cureent = itemchilds.item(j);
                        if (cureent.getNodeName().equalsIgnoreCase("title")) {
                            item.setTitle(cureent.getTextContent());
                        } else if (cureent.getNodeName().equalsIgnoreCase("description")) {
                            item.setDescription(cureent.getTextContent().replaceAll("<img.*?/>", ""));
                            Elements maybeImg = Jsoup.parse(cureent.getTextContent()).getElementsByTag("img");
                            if(!maybeImg.isEmpty())
                                item.setThumbnailUrl(maybeImg.first().attr("src"));
                            else
                                item.setThumbnailUrl("drawable/rss_logo.gif");
                        } else if (cureent.getNodeName().equalsIgnoreCase("pubDate")) {
                            item.setPubDate(cureent.getTextContent());
                        } else if (cureent.getNodeName().equalsIgnoreCase("link")) {
                            item.setLink(cureent.getTextContent());
                        }
                    }
                    if(item.getDescription().length() > 20)
                        temp_feedItems.add(item);
                }
            }
            feedItems.clear();
            feedItems.addAll(temp_feedItems);
        }
    }

    //This method will download rss feed document from specified url
    public Document Getdata(String address) {
        Log.d("ReadRSS", "GetData");
        try {
            url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDoc = builder.parse(inputStream);
            return xmlDoc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}