package com.example.s_tools.entertainment.Fragemnts.movies.google_dict;

import androidx.annotation.Keep;

@Keep
class Googlemodel {
    private String id;
    private String name;
    private String mimeType;
    private String modifiedTime;

    private String webContentLink;
    private String fileExtension;
    private String thumbnailLink;
    private String size;

    public Googlemodel(String id, String name, String mimeType, String modifiedTime, String webContentLink, String fileExtension, String thumbnailLink, String size) {
        this.id=id;
        this.name=name;
        this.mimeType=mimeType;
        this.modifiedTime=modifiedTime;
        this.webContentLink=webContentLink;
        this.fileExtension=fileExtension;
        this.thumbnailLink=thumbnailLink;
        this.size=size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType=mimeType;
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime=modifiedTime;
    }

    public String getWebContentLink() {
        return webContentLink;
    }

    public void setWebContentLink(String webContentLink) {
        this.webContentLink=webContentLink;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension=fileExtension;
    }

    public String getThumbnailLink() {
        return thumbnailLink;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink=thumbnailLink;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size=size;
    }
}
