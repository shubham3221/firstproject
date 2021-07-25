package com.example.s_tools.premium.model;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class PremiumModel {
    private String status;
    private int count;
    private int pages;
    private List<Post> posts = null;

    public PremiumModel(String status, int count, int pages, List<Post> posts) {
        this.status=status;
        this.count=count;
        this.pages=pages;
        this.posts=posts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status=status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count=count;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages=pages;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts=posts;
    }
}
