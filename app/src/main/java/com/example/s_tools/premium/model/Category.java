package com.example.s_tools.premium.model;

import androidx.annotation.Keep;

@Keep
public class Category {
    private int id;
    private int parent;

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent=parent;
    }

    public Category(int id) {
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }
}
