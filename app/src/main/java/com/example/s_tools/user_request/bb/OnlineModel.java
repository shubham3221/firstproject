package com.example.s_tools.user_request.bb;

import androidx.annotation.Keep;

@Keep
public class OnlineModel {
    private String url;
    private String title;
    private String imageurl;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date=date;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl=imageurl;
    }

    public OnlineModel(String url, String title, String imageurl, String date) {
        this.url=url;
        this.title=title;
        this.imageurl=imageurl;
        this.date=date;
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
}
