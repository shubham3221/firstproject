package com.example.s_tools.entertainment.Fragemnts.movies.google_dict;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class Modelclass {
    private String nextPageToken;
    private List<Googlemodel> files;

    public Modelclass(String nextPageToken, List<Googlemodel> files) {
        this.nextPageToken=nextPageToken;
        this.files=files;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken=nextPageToken;
    }

    public List<Googlemodel> getFiles() {
        return files;
    }

    public void setFiles(List<Googlemodel> files) {
        this.files=files;
    }
}
