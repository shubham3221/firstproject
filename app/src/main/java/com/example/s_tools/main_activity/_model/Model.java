package com.example.s_tools.main_activity._model;

import androidx.annotation.Keep;

@Keep
public class Model {
    private int image;
    private String first,second;

    public Model(int image,  String first, String second) {
        this.image=image;
        this.first=first;
        this.second=second;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image=image;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first=first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second=second;
    }
}
