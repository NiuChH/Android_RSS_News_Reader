package com.java.niuchenhao;

import java.util.UUID;

public class ChannelItem {
    private UUID id;
    private String title;
    private String xmlUrl;
    private int clickCount;

    public ChannelItem(){
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getXmlUrl() {
        return xmlUrl;
    }

    public void setXmlUrl(String xmlUrl) {
        this.xmlUrl = xmlUrl;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void addClickCount(int clickCount) {
        this.clickCount += 1;
    }
}
