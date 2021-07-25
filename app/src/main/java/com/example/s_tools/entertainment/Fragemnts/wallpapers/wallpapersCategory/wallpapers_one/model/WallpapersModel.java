package com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.model;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class WallpapersModel {
    List<WallpaperOneModel> previews;
WallpaperNextPage paging;

    public WallpapersModel(List<WallpaperOneModel> previews, WallpaperNextPage paging) {
        this.previews=previews;
        this.paging=paging;
    }

    public List<WallpaperOneModel> getPreviews() {
        return previews;
    }

    public void setPreviews(List<WallpaperOneModel> previews) {
        this.previews=previews;
    }

    public WallpaperNextPage getPaging() {
        return paging;
    }

    public void setPaging(WallpaperNextPage paging) {
        this.paging=paging;
    }
}
