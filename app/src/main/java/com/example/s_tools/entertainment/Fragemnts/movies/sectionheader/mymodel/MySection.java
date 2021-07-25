package com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class MySection {
    private int id;
    private String sectionName;
    private List<MoviesPosts> moviesPosts;

    public List<MoviesPosts> getMoviesPosts() {
        return moviesPosts;
    }

    public void setMoviesPosts(List<MoviesPosts> moviesPosts) {
        this.moviesPosts=moviesPosts;
    }

    public MySection(int id, String sectionName, List<MoviesPosts> sectionModels) {
        this.id=id;
        this.sectionName=sectionName;
        this.moviesPosts=sectionModels;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName=sectionName;
    }

}
