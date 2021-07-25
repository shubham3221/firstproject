package com.example.s_tools.entertainment.Fragemnts.movies.collection.collectionModel;

import androidx.annotation.Keep;

@Keep
public class SearchApiModel {
    private int id;
    private String title;

    public SearchApiModel(int id, String title) {
        this.id=id;
        this.title=title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }
}
