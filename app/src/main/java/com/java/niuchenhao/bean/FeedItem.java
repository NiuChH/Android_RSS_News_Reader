package com.java.niuchenhao.bean;

import android.support.annotation.NonNull;
import android.util.Log;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static android.support.constraint.Constraints.TAG;

public class FeedItem extends LitePalSupport implements Serializable {

    @Override
    public int hashCode() {
        return getLink().hashCode();
    }

    @Column(unique = true)
    private String title;
    @Column(unique = true)
    private String link;
    private String description;
    private String pubDate;
    private long longDate;
    private String thumbnailUrl;
    private boolean hadRead;
    private boolean hadReadDescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FeedItem)) return false;
        FeedItem feedItem = (FeedItem) o;
        return getLink().equals(feedItem.getLink());
    }

    @Override
    public String toString() {
        return "FeedItem{" +

                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", longDate=" + longDate +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", hadRead=" + hadRead +
                ", hadReadDescription=" + hadReadDescription +
                ", favourite=" + favourite +
                ", channelTitle='" + channelTitle + '\'' +
                '}';
    }

    private boolean favourite;

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    private String channelTitle;

    public FeedItem(){
        hadRead = false;
        hadReadDescription = false;
    }

    public boolean isHadRead() {
        return hadRead;
    }

    public boolean isHadReadDescription() {
        return hadReadDescription;
    }

    public void setHadRead(boolean hadRead){
        this.hadRead = hadRead;
    }

    public boolean hadRead() {
        return hadRead;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
        try {
            this.longDate = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(pubDate).getTime();
        } catch (ParseException e) {
            try {
                this.longDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US).parse(pubDate).getTime();
            } catch (ParseException e1) {
                e1.printStackTrace();
                Log.d(TAG, "ERROR: "+pubDate);
            }
        }
    }


    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public boolean hadReadDescription() {
        return hadReadDescription;
    }

    public void setHadReadDescription(boolean hadReadDescription) {
        this.hadReadDescription = hadReadDescription;
    }


    public long getLongDate() {
        return longDate;
    }

    public void setLongDate(long longDate) {
        this.longDate = longDate;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getFilename(){
        return link.replaceAll("[^a-zA-Z0-9]", "");
    }
    //    @Override
//    public <T> boolean isSameModelAs(@NonNull T model) {
//        return model instanceof FeedItem && ((FeedItem) model).getLink().equals(link);
//    }
//
//    @Override
//    public <T> boolean isContentTheSameAs(@NonNull T model) {
//        return true;
//    }
}