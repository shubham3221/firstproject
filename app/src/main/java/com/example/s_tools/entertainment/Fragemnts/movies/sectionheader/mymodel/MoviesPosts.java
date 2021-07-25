package com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel;

import androidx.annotation.Keep;

import com.example.s_tools.premium.model.Category;

import java.util.List;

@Keep
public class MoviesPosts {
    private int id;
    private String title;
    private Cfields custom_fields;
    private String date;
    private List<Category> categories;

    public MoviesPosts(int id) {
        this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public Cfields getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(Cfields custom_fields) {
        this.custom_fields=custom_fields;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date=date;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories=categories;
    }
}
