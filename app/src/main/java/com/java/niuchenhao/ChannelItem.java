package com.java.niuchenhao;

import java.util.UUID;

public class ChannelItem {
    private UUID id;
    private String title;
    private String xmlUrl;
    private int clickCount;
    private boolean checked;

    public ChannelItem(String title, String xmlUrl){
        id = UUID.randomUUID();
        clickCount = 0;
        this.title = title;
        this.xmlUrl = xmlUrl;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getXmlUrl() {
        return xmlUrl;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void addClickCount() {
        this.clickCount += 1;
    }

    public boolean isChecked() {
        return checked;
    }

    public void toggleChecked() {
        this.checked = !this.checked;
    }
}
