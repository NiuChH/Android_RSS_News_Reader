package com.java.niuchenhao.utils;

import android.content.Context;
import android.util.Log;

import com.java.niuchenhao.R;
import com.java.niuchenhao.bean.ChannelItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// TODO move it to server
public class OpmlReader {

    private OpmlReader(){
        throw new AssertionError("OpmlReader is a static class");
    }

    private static ArrayList<ChannelItem> li = new ArrayList<>();

    public static List<ChannelItem> readData(Context context){
        InputStream inStream;
        String data = null;
        try {
            inStream = context.getAssets().open(context.getResources().getString(R.string.opml_filename));     //打开assets目录中的文本文件
            byte[] bytes = new byte[inStream.available()];  //inStream.available()为文件中的总byte数
            inStream.read(bytes);
            inStream.close();
            data = new String(bytes, "utf-8");        //将bytes转为utf-8字符串
        } catch (IOException e) {
            Log.e("OpmlReader", e.toString());
            e.printStackTrace();
        }
        if(data == null)
            return li;
        Document doc = Jsoup.parse(data);
        JSONObject json = new JSONObject();
        String text, xmlUrl;
        Elements eles = doc.body().getElementsByTag("outline");
        for(Element ele: eles){
            if(ele.attr("type").equalsIgnoreCase("rss")){
                text = ele.attr("text");
                xmlUrl = ele.attr("xmlUrl");
                li.add(new ChannelItem(text, xmlUrl));
            }
        }
//        Log.d("OpmlReader", li.toString());
//        try {
//            FileWriter fos = new FileWriter(fileName.replace("xml", "txt"));
//            fos.write(json.toString());
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return li;
    }

}
