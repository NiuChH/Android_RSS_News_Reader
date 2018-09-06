package com.java.niuchenhao;

import java.util.UUID;

/**
 * Created by rishabh on 24-02-2016.
 */
public class FeedItem {
    private UUID id;
    private String title;
    private String link;
    private String description;
    private String pubDate;
    private String thumbnailUrl;
    private boolean hadRead;
    private ChannelItem channelItem;

    public FeedItem(){
        id = UUID.randomUUID();
        hadRead = false;
    }

    public void toggleHadRead() {
        hadRead = !hadRead;
    }

    public boolean getHadRead() {
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
}