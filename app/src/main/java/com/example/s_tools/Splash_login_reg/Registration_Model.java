package com.example.s_tools.Splash_login_reg;

import androidx.annotation.Keep;

@Keep
public class Registration_Model {
    private String status;
    private String error;

    public Registration_Model(String status, String error) {
        this.status=status;
        this.error=error;
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
}
