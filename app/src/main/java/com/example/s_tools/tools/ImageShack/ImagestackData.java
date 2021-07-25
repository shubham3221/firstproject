package com.example.s_tools.tools.ImageShack;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class ImagestackData {
    Getalbum result;

    public ImagestackData(Getalbum result) {
        this.result=result;
    }

    public Getalbum getAlbums() {
        return result;
    }

    public void setAlbums(Getalbum result) {
        this.result=result;
    }

    //
    public class Getalbum {
        private List<Myalbum> albums;

        public Getalbum(List<Myalbum> albums) {
            this.albums=albums;
        }

        public List<Myalbum> getImages() {
            return albums;
        }

        public void setImages(List<Myalbum> albums) {
            this.albums=albums;
        }

        //
        public class Myalbum{
            private String id;
            private String title;
            private List<AlbumImages> images;

            public Myalbum(String id, String title, List<AlbumImages> images) {
                this.id=id;
                this.title=title;
                this.images=images;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id=id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title=title;
            }

            public List<AlbumImages> getImages() {
                return images;
            }

            public void setImages(List<AlbumImages> images) {
                this.images=images;
            }

            //
            public class AlbumImages {
                private String id;
                private int server;
                private int bucket;
                private String filename;
                private String direct_link;
                private String original_filename;
                private String title;
                private boolean adult_content;

                public AlbumImages(String id, int server, int bucket, String filename, String direct_link, String original_filename, String title, boolean adult_content) {
                    this.id=id;
                    this.server=server;
                    this.bucket=bucket;
                    this.filename=filename;
                    this.direct_link=direct_link;
                    this.original_filename=original_filename;
                    this.title=title;
                    this.adult_content=adult_content;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id=id;
                }

                public int getServer() {
                    return server;
                }

                public void setServer(int server) {
                    this.server=server;
                }

                public int getBucket() {
                    return bucket;
                }

                public void setBucket(int bucket) {
                    this.bucket=bucket;
                }

                public String getFilename() {
                    return filename;
                }

                public void setFilename(String filename) {
                    this.filename=filename;
                }

                public String getDirect_link() {
                    return direct_link;
                }

                public void setDirect_link(String direct_link) {
                    this.direct_link=direct_link;
                }

                public String getOriginal_filename() {
                    return original_filename;
                }

                public void setOriginal_filename(String original_filename) {
                    this.original_filename=original_filename;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title=title;
                }

                public boolean isAdult_content() {
                    return adult_content;
                }

                public void setAdult_content(boolean adult_content) {
                    this.adult_content=adult_content;
                }
            }
        }



    }

}
