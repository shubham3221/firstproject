package com.example.s_tools;

import androidx.annotation.Keep;

@Keep
public class JwtModel {
    private String token;
    private String user_email;

    public JwtModel(String token, String user_email) {
        this.token=token;
        this.user_email=user_email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token=token;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email=user_email;
    }
}
