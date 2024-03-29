package com.java.niuchenhao.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.java.niuchenhao.model.bean.ChannelItem;
import com.java.niuchenhao.model.bean.FeedItem;
import com.java.niuchenhao.presenter.ChannelsPresenter;
import com.java.niuchenhao.presenter.FeedsPresenter;
import com.java.niuchenhao.utils.OpmlReader;
import com.java.niuchenhao.utils.ReadRss;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;
import org.litepal.crud.callback.FindMultiCallback;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class DatabaseModel {
    final private static String URL_STRING = "http://183.172.152.155:9999";
    final private static Gson gson = new Gson();
    private static DatabaseModel databaseModel = new DatabaseModel();
    private static boolean isOnline;
    private static boolean useServer;

    private DatabaseModel() {
        ConnectivityManager connectivityManager = (ConnectivityManager) LitePalApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        isOnline = (connectivityManager != null &&
                connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isAvailable());
        useServer = false;
        Log.i("is online: ", String.valueOf(isOnline));
        Log.i("using server: ", String.valueOf(useServer));
    }

    private static FindMultiCallback getNotifyCallBack(final ChannelItem channelItem, final Boolean isAppend, final List<FeedItem> feedItemList) {
        return new FindMultiCallback() {
            @Override
            public <T> void onFinish(List<T> t) {
                if (!isAppend)
                    feedItemList.clear();
                feedItemList.addAll((List<FeedItem>) t);
                Log.d(TAG, "FindMultiCallback");
                for (FeedItem fi : feedItemList) {
                    Log.d(TAG, fi.getTitle());
                }
                FeedsPresenter.notifyAdapter(channelItem);
            }
        };
    }

    /***********  for DB  ***********/


    private static void getFeedsAsyncFromDB(ChannelItem channelItem, String keyWord, Integer number, Boolean isAppend, List<FeedItem> feedItemList) {
        Integer offset = isAppend ? feedItemList.size() : 0;
        //TODO add sort by date
        if (keyWord == null) {
            Log.d(TAG, channelItem.getTitle());
            LitePal.where("channeltitle = ?", channelItem.getTitle())
                    .limit(number)
                    .offset(offset)
                    .findAsync(FeedItem.class)
                    .listen(getNotifyCallBack(channelItem, isAppend, feedItemList));
        } else
            LitePal.where("title like ?", keyWord)
                    .limit(number)
                    .offset(offset)
                    .findAsync(FeedItem.class)
                    .listen(getNotifyCallBack(channelItem, isAppend, feedItemList));
    }

    /***********  for Presenters  ***********/

    public static void updateFeedItem(FeedItem feedItem) {
        feedItem.saveOrUpdate("link = ?", feedItem.getLink());
    }

    public static void updateFeedItem(Collection<FeedItem> feedItems) {
        for (FeedItem feedItem : feedItems)
            updateFeedItem(feedItem);
    }

    public static void updateChannelItem(ChannelItem channelItem) {
        channelItem.saveOrUpdate("xmlurl = ?", channelItem.getXmlUrl());
    }

    public static void updateChannelItem(Collection<ChannelItem> channelItems) {
        for (ChannelItem channelItem : channelItems)
            updateChannelItem(channelItem);
    }

    public static List<ChannelItem> getChannelsSync(Context context) {
        List<ChannelItem> result = OpmlReader.readData(context);
        ChannelItem ciDB;
        for (ChannelItem channelItem : result) {
            ciDB = LitePal.where("xmlurl = ?", channelItem.getXmlUrl()).findFirst(ChannelItem.class);
            if (ciDB == null) {
                // 出现这种情况是改变了语言设置
                LitePal.deleteAll(ChannelItem.class);
                LitePal.saveAll(result);
                break;
            } else {
                channelItem.setClickCount(ciDB.getClickCount());
                channelItem.setChecked(ciDB.isChecked());
            }
        }

        updateChannelItem(result);
        return result;
    }

    public static void getFeedsAsync(final ChannelItem channelItem, final String keyWord, final Integer number, final Boolean isAppend, final List<FeedItem> feedItemList) {
        if (channelItem.getTitle().equals(ChannelsPresenter.getRecommendChannelItem().getTitle())) {
            getRecommend(channelItem, feedItemList);
        } else if ((!useServer) && isOnline && keyWord == null) {
            new ReadRss(channelItem, feedItemList).execute(number, isAppend ? 1 : 0);
            Log.d(TAG, "using ReadRss");
            Log.d(TAG, useServer + " " + isOnline);
        } else if (useServer && isOnline) {
            final String[] headers = {
                    URL_STRING,
                    channelItem.getXmlUrl(),
                    keyWord,
                    number.toString(),
                    isAppend.toString(),
                    String.valueOf(feedItemList.size())};
            sendOkHttpRequest(headers, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    useServer = false;
                    getFeedsAsyncFromDB(channelItem, keyWord, number, isAppend, feedItemList);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (!isAppend)
                        feedItemList.clear();
                    List<FeedItem> result = gson.fromJson(response.body().string(),
                            new TypeToken<FeedItem>() {
                            }.getType());
                    feedItemList.addAll(result);
                    FeedsPresenter.notifyAdapter(channelItem);
                    for (FeedItem feedItem : result)
                        try {
                            feedItem.save();
                        } catch (Exception ignore) {
                        }
                }
            });
        } else {
            getFeedsAsyncFromDB(channelItem, keyWord, number, isAppend, feedItemList);
        }
    }

    public static void getFavourites(final List<FeedItem> feedItemList) {
        LitePal.where("favourite = 1")
                .findAsync(FeedItem.class)
                .listen(getNotifyCallBack(null, false, feedItemList));
    }

    public static void getRecommend(final ChannelItem channelItem, final List<FeedItem> feedItemList) {
        LitePal.order("clickcount desc").limit(3).findAsync(ChannelItem.class).listen(new FindMultiCallback() {
            @Override
            public <T> void onFinish(List<T> t) {
                Log.d("Recommend", ((ChannelItem) t.get(0)).getTitle());
                Log.d("Recommend", ((ChannelItem) t.get(1)).getTitle());
                Log.d("Recommend", ((ChannelItem) t.get(2)).getTitle());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    LitePal.select("channeltitle").find(FeedItem.class).forEach(v ->
                            Log.d("Recommend", v.toString()));
                }

                List<FeedItem> result = LitePal.order("longdate desc")
                        .where("channeltitle in (?,?,?)",
                                ((ChannelItem) t.get(0)).getTitle(),
                                ((ChannelItem) t.get(1)).getTitle(),
                                ((ChannelItem) t.get(2)).getTitle())
                        .offset(feedItemList.size())
                        .limit(10)
                        .find(FeedItem.class);
                if (result.size() < 5) {
                    LitePal.where("channeltitle = ?", ((ChannelItem) t.get(0)).getTitle())
                            .limit(10)
                            .offset(feedItemList.size())
                            .findAsync(FeedItem.class)
                            .listen(getNotifyCallBack(channelItem, true, feedItemList));
                } else {
                    feedItemList.addAll(result);
                    FeedsPresenter.notifyAdapter(channelItem);
                }
            }
        });
    }

    /***********  for server  ***********/

    private static void sendOkHttpRequest(final String[] args, final okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder()
                .url(args[0])
                .addHeader("xmlUrl", args[1]);
        if (args[2] != null)
            requestBuilder.addHeader("keyWord", args[2]);
        client.newCall(
                requestBuilder
                        .addHeader("number", args[3])
                        .addHeader("isAppend", args[4])
                        .addHeader("offset", args[5])
                        .build()
        ).enqueue(callback);
    }

    public static boolean getIsOnline() {
        return isOnline;
    }

    public static void setIsOnline(boolean isOnline) {
        DatabaseModel.isOnline = isOnline;
    }

    /************* for debug **************/

    public static void saveTestFeedItem(ChannelItem channelItem) {
        FeedItem feedItem = new FeedItem();
        feedItem.setDescription("aaa");
//        feedItem.setChannelXmlUrl(channelItem.getXmlUrl());
        feedItem.save();
    }
}