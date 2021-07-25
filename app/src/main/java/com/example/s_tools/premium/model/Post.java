package com.example.s_tools.premium.model;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class Post {
    private int id;
    private String excerpt;
    private String date;
    private String modified;
    private List<Category> categories = null;
    private Custom_Fields custom_fields = null;

    public Post(int id, String excerpt, String date, String modified, List<Category> categories, Custom_Fields custom_fields) {
        this.id=id;
        this.excerpt=excerpt;
        this.date=date;
        this.modified=modified;
        this.categories=categories;
        this.custom_fields=custom_fields;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt=excerpt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date=date;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified=modified;
    }


    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories=categories;
    }

    public Custom_Fields getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(Custom_Fields custom_fields) {
        this.custom_fields=custom_fields;
    }
}
