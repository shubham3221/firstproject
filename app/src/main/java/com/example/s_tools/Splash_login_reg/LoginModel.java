package com.example.s_tools.Splash_login_reg;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class LoginModel {
    private String status;
    private String error;
    private String cookie;
    @SerializedName("user")
    private User userData;

    public LoginModel(String status, String error, String cookie, User userData) {
        this.status=status;
        this.error=error;
        this.cookie=cookie;
        this.userData=userData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status=status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error=error;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie=cookie;
    }

    public User getUserData() {
        return userData;
    }

    public void setUserData(User userData) {
        this.userData=userData;
    }
}
