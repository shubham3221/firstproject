package com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class MainModel {
    List<MoviesPosts> posts;

    public MainModel(List<MoviesPosts> posts) {
        this.posts=posts;
    }

    public List<MoviesPosts> getPosts() {
        return posts;
    }

    public void setPosts(List<MoviesPosts> posts) {
        this.posts=posts;
    }
}
