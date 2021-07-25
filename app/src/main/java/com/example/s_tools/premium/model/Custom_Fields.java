package com.example.s_tools.premium.model;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class Custom_Fields {
    private List<String> link = null;
    private List<String> mainimage = null;

    public List<String> getMainimage() {
        return mainimage;
    }

    public Custom_Fields(List<String> link) {
        this.link=link;
    }

    public List<String> getLink() {
        return link;
    }

    public void setLink(List<String> link) {
        this.link=link;
    }
}
