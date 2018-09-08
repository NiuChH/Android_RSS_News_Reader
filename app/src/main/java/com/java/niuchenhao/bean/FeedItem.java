package com.java.niuchenhao.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

public class FeedItem extends LitePalSupport implements Serializable {

    @Column(unique = true)
    private String title;
    @Column(unique = true)
    private String link;
    private String description;
    private Date pubDate;
    private String thumbnailUrl;
    private boolean hadRead;
    private boolean hadReadDescription;
    private ChannelItem channelItem;

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

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = Date.valueOf(pubDate);
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public ChannelItem getChannelItem() {
        return channelItem;
    }

    public void setChannelItem(ChannelItem channelItem) {
        this.channelItem = channelItem;
    }

    public boolean hadReadDescription() {
        return hadReadDescription;
    }

    public void setHadReadDescription(boolean hadReadDescription) {
        this.hadReadDescription = hadReadDescription;
    }


}