package com.example.s_tools.Splash_login_reg;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class User {
    private int id;
    private String username;
    private String firstname;
    private String email;
    @SerializedName("registered")
    private String dateofRegistered;

    public User(int id, String username, String firstname, String email, String dateofRegistered) {
        this.id=id;
        this.username=username;
        this.firstname=firstname;
        this.email=email;
        this.dateofRegistered=dateofRegistered;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username=username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname=firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email=email;
    }

    public String getDateofRegistered() {
        return dateofRegistered;
    }

    public void setDateofRegistered(String dateofRegistered) {
        this.dateofRegistered=dateofRegistered;
    }
}
