package com.example.s_tools.main_activity.mainactivity_toolbar_api;

import androidx.annotation.Keep;

@Keep
public class ChangeNameModel {
    private String status;

    public ChangeNameModel(String status) {
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status=status;
    }
}
