package com.example.s_tools.Services_item.mp3download;

import androidx.annotation.Keep;

@Keep
public class Model {
    String url;
    String size;

    public Model(String url, String size) {
        this.url=url;
        this.size=size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url=url;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size=size;
    }

}
