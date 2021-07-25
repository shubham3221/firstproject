package com.example.s_tools.testing;


import androidx.annotation.Keep;

@Keep
public class Downloadmodel {
    private String url;
    private String title;
    private int progress;
    private int status;
    private String file_size;
    private String path;

    public Downloadmodel(String url, String title, int progress, int status, String file_size, String path) {
        this.url=url;
        this.title=title;
        this.progress=progress;
        this.status=status;
        this.file_size=file_size;
        this.path=path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url=url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress=progress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status=status;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size=file_size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path=path;
    }
}
