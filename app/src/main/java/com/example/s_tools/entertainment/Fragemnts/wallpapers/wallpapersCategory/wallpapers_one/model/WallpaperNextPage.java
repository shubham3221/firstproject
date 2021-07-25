package com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.model;

import androidx.annotation.Keep;

@Keep
public class WallpaperNextPage {
    String prev;
    String next;

    public WallpaperNextPage(String prev, String next) {
        this.prev=prev;
        this.next=next;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev=prev;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next=next;
    }
}
