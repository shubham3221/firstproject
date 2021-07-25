package com.example.s_tools.entertainment.Fragemnts.movies.sectionheader;

import androidx.annotation.Keep;

import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MoviesPosts;

import java.util.List;

@Keep
public class SModel {
    private int count_total;
    private List<MoviesPosts> posts;

    public SModel(int count_total, List<MoviesPosts> posts) {
        this.count_total=count_total;
        this.posts=posts;
    }

    public int getCount_total() {
        return count_total;
    }

    public void setCount_total(int count_total) {
        this.count_total=count_total;
    }

    public List<MoviesPosts> getPosts() {
        return posts;
    }

    public void setPosts(List<MoviesPosts> posts) {
        this.posts=posts;
    }
}
