package com.example.s_tools.entertainment.Fragemnts.movies.collection.collectionModel;

import android.os.Parcel;
import android.os.Parcelable;

public class Title implements Parcelable {


    private String rendered;
    public Title() {
    }
    public Title(String rendered) {
        super();
        this.rendered = rendered;
    }

    public String getRendered() {
        return rendered;
    }

    public void setRendered(String rendered) {
        this.rendered = rendered;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.rendered);
    }

    protected Title(Parcel in) {
        this.rendered=in.readString();
    }

    public static final Creator<Title> CREATOR=new Creator<Title>() {
        @Override
        public Title createFromParcel(Parcel source) {
            return new Title(source);
        }

        @Override
        public Title[] newArray(int size) {
            return new Title[size];
        }
    };
}
