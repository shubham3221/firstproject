package com.example.s_tools.entertainment.uper_slider;

import androidx.annotation.Keep;

@Keep
public class SlideModel {
    private String imgurl;
    private String name;

    public SlideModel(String imgurl, String name) {
        this.imgurl=imgurl;
        this.name=name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl=imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }
}
