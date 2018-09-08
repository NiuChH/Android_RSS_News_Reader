package com.java.niuchenhao;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.java.niuchenhao.bean.ChannelItem;
import com.java.niuchenhao.bean.FeedItem;
import com.java.niuchenhao.utils.OpmlReader;

import org.litepal.LitePal;
import org.litepal.LitePalDB;
import org.litepal.crud.callback.FindMultiCallback;
import org.litepal.parser.LitePalParser;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DatabaseModel {
    private static DatabaseModel databaseModel = new DatabaseModel();

    private static boolean isOnline = true;

    final private static String URL_STRING = "http://localhost";

    final private static Gson gson = new Gson();

    private DatabaseModel(){}

    private static FindMultiCallback getNotifyCallBack(final ChannelItem channelItem, final Boolean isAppend, final List<FeedItem> feedItemList){
        return new FindMultiCallback() {
            @Override
            public <T> void onFinish(List<T> t) {
                if(!isAppend)
                    feedItemList.clear();
                feedItemList.addAll((List<FeedItem>)t);
                FeedsPresenter.notifyAdapter(channelItem);
            }
        };
    }

    /***********  for DB  ***********/


    private static void getFeedsAsyncFromDB(ChannelItem channelItem, String keyWord, Integer number, Boolean isAppend, List<FeedItem> feedItemList) {
        if(keyWord == null){
            if(isAppend)
                LitePal.order("pubDate desc").limit(number).offset(feedItemList.size()).findAsync(FeedItem.class).listen(getNotifyCallBack(channelItem, true, feedItemList));
            else
                LitePal.order("pubDate desc").limit(number).findAsync(FeedItem.class).listen(getNotifyCallBack(channelItem, false, feedItemList));
        }else if (isAppend)
            LitePal.order("pubDate desc").limit(number).offset(feedItemList.size()).where("title contains ?", keyWord).findAsync(FeedItem.class).listen(getNotifyCallBack(channelItem, true, feedItemList));
        else
            LitePal.order("pubDate desc").limit(number).where("title contains ?", keyWord).findAsync(FeedItem.class).listen(getNotifyCallBack(channelItem, false, feedItemList));
    }

//    private static void addAll(List<LitePalSupport> items){
//        LitePal.saveAllAsync(items);
//    }

    /***********  for Presenters  ***********/

    public static List<ChannelItem> getChannelsSync(Context context){
        List<ChannelItem> result = OpmlReader.readData(context);
        try {
            LitePal.saveAll(result);  // TODO is this necessary?
        } catch (Exception ignored){}
        return result;
    }

    public static void getFeedsAsync(final ChannelItem channelItem, final String keyWord, final Integer number, final Boolean isAppend, final List<FeedItem> feedItemList){
        if(isOnline){
            final String [] headers = {
                    URL_STRING,
                    channelItem.getXmlUrl(),
                    keyWord,
                    number.toString(),
                    isAppend.toString(),
                    String.valueOf(feedItemList.size())};
//            getFeedsAsyncFromServer(channelItem, keyWord, number, isAppend, feedItemList);
            sendOkHttpRequest(headers, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    // TODO this is for DEBUG
                    new FeedItem().save();
//                    getFeedsAsyncFromDB(channelItem, keyWord, number, isAppend, feedItemList);
                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(!isAppend)
                        feedItemList.clear();
                    List<FeedItem> result = gson.fromJson(response.body().string(),
                            new TypeToken<FeedItem>(){}.getType());
                    feedItemList.addAll(result);
                    FeedsPresenter.notifyAdapter(channelItem);
                    for(FeedItem feedItem: result)
                        if(!LitePal.isExist(FeedItem.class, "link = ?", feedItem.getLink()))
                            feedItem.save();
//                    LitePal.saveAll(result);
                }
            });
        } else {
            getFeedsAsyncFromDB(channelItem, keyWord, number, isAppend, feedItemList);
        }
    }

    /***********  for server  ***********/

//    private static void getFeedsAsyncFromServer(ChannelItem channelItem, String keyWord, Integer number, Boolean isAppend, List<FeedItem> feedItemList){
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                try {
//                    URL url = new URL(URL_STRING);
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        }.execute();
//    }

//    private class GetFeedsAsyncFromServerTask extends AsyncTask<Object, Void, Boolean> {
//        URL url = null;
//        HttpURLConnection connection = null;
//
//        @Override
//        protected Boolean doInBackground(Object... objects) {
//            try {
////                url = new URL(URL_STRING);
////                connection = (HttpURLConnection) url.openConnection();
////                connection.setRequestMethod("GET");
////                connection.setRequestProperty();
////                connection.setConnectTimeout(2000);
////                connection.setReadTimeout(2000);
////                InputStream in = connection.getInputStream();
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (ProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//
//        }
//    }

    public static void sendOkHttpRequest(final String[] args, final okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder()
                .url(args[0])
                .addHeader("xmlUrl", args[1]);
        if(args[2] != null)
            requestBuilder.addHeader("keyWord", args[2]);
        client.newCall(
                requestBuilder
                    .addHeader("number", args[3])
                    .addHeader("isAppend", args[4])
                    .addHeader("offset", args[5])
                    .build()
        ).enqueue(callback);
    }
}
