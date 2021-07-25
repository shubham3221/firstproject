package com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel;

import androidx.annotation.Keep;

@Keep
public class SectionModel {
    private int postid;
    private int category;

    private String mainImageUrl;
    private String rating;
    private String title;
    private String review;

    public SectionModel(int postid, int category, String mainImageUrl, String rating, String title, String review) {
        this.postid=postid;
        this.category=category;
        this.mainImageUrl=mainImageUrl;
        this.rating=rating;
        this.title=title;
        this.review=review;
    }

    public int getPostid() {
        return postid;
    }

    public void setPostid(int postid) {
        this.postid=postid;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category=category;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl=mainImageUrl;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating=rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review=review;
    }
}
