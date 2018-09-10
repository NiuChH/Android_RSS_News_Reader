package com.java.niuchenhao.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.UUID;

public class ChannelItem extends LitePalSupport implements Serializable {

    @Column(unique = true)
    private String title;
    @Column(unique = true)
    private String xmlUrl;
    private int clickCount;
    private boolean checked;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChannelItem)) return false;
        ChannelItem channelItem = (ChannelItem) o;
        return getXmlUrl().equals(channelItem.getXmlUrl());
    }

    public ChannelItem(String title, String xmlUrl){
        clickCount = 0;
        this.title = title;
        this.xmlUrl = xmlUrl;
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

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void addClickCount() {
        this.clickCount += 1;
    }

    public void toggleChecked() {
        this.checked = !this.checked;
    }
}
