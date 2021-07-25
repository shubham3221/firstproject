package com.example.s_tools.entertainment.Fragemnts.movies.collection.collectionModel;

import androidx.annotation.Keep;

@Keep
public class MoviesModel{
    private int id;
    private Title title;
    private Content content;
    private String mainimg;
    private String modified;
    private String link;

    public MoviesModel(int id, Title title, Content content, String mainimg, String modified, String link) {
        this.id=id;
        this.title=title;
        this.content=content;
        this.mainimg=mainimg;
        this.modified=modified;
        this.link=link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title=title;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content=content;
    }

    public String getMainimg() {
        return mainimg;
    }

    public void setMainimg(String mainimg) {
        this.mainimg=mainimg;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified=modified;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link=link;
    }
}
