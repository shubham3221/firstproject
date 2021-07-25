package com.example.s_tools.user_request.bb;

import androidx.annotation.Keep;

@Keep
 class Modeldata {
    private String url;

     public Modeldata(String url) {
         this.url=url;
     }

     public String getUrl() {
         return url;
     }

     public void setUrl(String url) {
         this.url=url;
     }
 }
