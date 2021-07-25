package com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_two;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

@Keep
public class Wallpapers_2_Model implements Parcelable {
    private String thumbUrl;
    private String imgUrl;

    public Wallpapers_2_Model(String thumbUrl, String imgUrl) {
        this.thumbUrl=thumbUrl;
        this.imgUrl=imgUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl=thumbUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl=imgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.thumbUrl);
        dest.writeString(this.imgUrl);
    }

    protected Wallpapers_2_Model(Parcel in) {
        this.thumbUrl=in.readString();
        this.imgUrl=in.readString();
    }

    public static final Creator<Wallpapers_2_Model> CREATOR=new Creator<Wallpapers_2_Model>() {
        @Override
        public Wallpapers_2_Model createFromParcel(Parcel source) {
            return new Wallpapers_2_Model(source);
        }

        @Override
        public Wallpapers_2_Model[] newArray(int size) {
            return new Wallpapers_2_Model[size];
        }
    };
}
