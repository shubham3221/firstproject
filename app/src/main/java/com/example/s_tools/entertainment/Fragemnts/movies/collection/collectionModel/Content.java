package com.example.s_tools.entertainment.Fragemnts.movies.collection.collectionModel;

import android.os.Parcel;
import android.os.Parcelable;

public class Content implements Parcelable {

    private String rendered;

    public Content(String rendered) {
        this.rendered=rendered;
    }

    public String getRendered() {
        return rendered;
    }

    public void setRendered(String rendered) {
        this.rendered=rendered;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.rendered);
    }

    protected Content(Parcel in) {
        this.rendered=in.readString();
    }

    public static final Creator<Content> CREATOR=new Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel source) {
            return new Content(source);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };
}