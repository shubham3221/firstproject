package com.example.s_tools.Services_item.VidDownloader;

import androidx.annotation.Keep;

@Keep
class VideoModel {
    private String title;
    private String id;
    private String size;
    private String quality;
    private String type;
    private String convertid;

    public VideoModel(String title, String id, String size, String quality, String type, String convertid) {
        this.title=title;
        this.id=id;
        this.size=size;
        this.quality=quality;
        this.type=type;
        this.convertid=convertid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id=id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size=size;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality=quality;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type=type;
    }

    public String getConvertid() {
        return convertid;
    }

    public void setConvertid(String convertid) {
        this.convertid=convertid;
    }
}
