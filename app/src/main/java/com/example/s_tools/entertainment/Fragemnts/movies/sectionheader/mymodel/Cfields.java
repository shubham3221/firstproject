package com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class Cfields {
    private List<String> mainimage;
    private List<String> screenshots;
    private List<String> rating;
    private List<String> review;
    private List<String> gen;

    private List<String> drive720;
    private List<String> drive1080;
    private List<String> otherlinks;
    private List<String> watchonline;

    public List<String> getWatchonline() {
        return watchonline;
    }

    public void setWatchonline(List<String> watchonline) {
        this.watchonline=watchonline;
    }

    public List<String> getDrive720() {
        return drive720;
    }

    public void setDrive720(List<String> drive720) {
        this.drive720=drive720;
    }

    public List<String> getDrive1080() {
        return drive1080;
    }

    public void setDrive1080(List<String> drive1080) {
        this.drive1080=drive1080;
    }

    public List<String> getMainimage() {
        return mainimage;
    }

    public void setMainimage(List<String> mainimage) {
        this.mainimage=mainimage;
    }

    public List<String> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<String> screenshots) {
        this.screenshots=screenshots;
    }

    public List<String> getRating() {
        return rating;
    }

    public void setRating(List<String> rating) {
        this.rating=rating;
    }

    public List<String> getReview() {
        return review;
    }

    public void setReview(List<String> review) {
        this.review=review;
    }


    public List<String> getOtherlinks() {
        return otherlinks;
    }

    public void setOtherlinks(List<String> otherlinks) {
        this.otherlinks=otherlinks;
    }

    public List<String> getGen() {
        return gen;
    }

    public void setGen(List<String> gen) {
        this.gen=gen;
    }
}
