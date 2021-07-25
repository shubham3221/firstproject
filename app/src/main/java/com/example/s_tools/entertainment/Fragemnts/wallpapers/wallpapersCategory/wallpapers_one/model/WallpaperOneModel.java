package com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

@Keep
public class WallpaperOneModel implements Parcelable {
    private int id;
    private String url;

    public WallpaperOneModel(int id, String url) {
        this.id=id;
        this.url=url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url=url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.url);
    }

    protected WallpaperOneModel(Parcel in) {
        this.id=in.readInt();
        this.url=in.readString();
    }

    public static final Creator<WallpaperOneModel> CREATOR=new Creator<WallpaperOneModel>() {
        @Override
        public WallpaperOneModel createFromParcel(Parcel source) {
            return new WallpaperOneModel(source);
        }

        @Override
        public WallpaperOneModel[] newArray(int size) {
            return new WallpaperOneModel[size];
        }
    };
}
