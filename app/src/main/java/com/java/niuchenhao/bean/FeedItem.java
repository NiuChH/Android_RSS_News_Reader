package com.java.niuchenhao.bean;

import java.io.Serializable;
import java.util.UUID;

public class FeedItem implements Serializable {
    private UUID id;
    private String title;
    private String link;
    private String description;
    private String pubDate;
    private String thumbnailUrl;
    private UUID channelId;
    private boolean hadRead;
    private boolean hadReadDescription;
    private ChannelItem channelItem;

    public FeedItem(){
        id = UUID.randomUUID();
        hadRead = false;
    }

    public void setHadRead(boolean hadRead){
        this.hadRead = hadRead;
    }

    public boolean hadRead() {
        return hadRead;
    }

    public UUID getId() { return id;}

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

    public UUID getChannelId() {
        return channelId;
    }

    public void setChannelId(UUID channelId) {
        this.channelId = channelId;
    }
}