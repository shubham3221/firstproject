package com.example.s_tools.premium.model;

import androidx.annotation.Keep;

@Keep
public class Attachments {
    private int id;
    private String url;
    private String title;
    private String description;

    public Attachments(int id, String url, String title, String description) {
        this.id=id;
        this.url=url;
        this.title=title;
        this.description=description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url=url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description=description;
    }
}
