package com.example.s_tools.chatting.messageModel;

import androidx.annotation.Keep;

@Keep
public class Autherror {
   private String code;

    public Autherror(String code) {
        this.code=code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code=code;
    }
}
