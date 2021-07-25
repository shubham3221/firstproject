package com.example.s_tools.premium.model;

import androidx.annotation.Keep;

@Keep
class Comments {
    private int id;
    private String name;
    private String date;
    private String content;

    public Comments(int id, String name, String date, String content) {
        this.id=id;
        this.name=name;
        this.date=date;
        this.content=content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date=date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content=content;
    }
}
