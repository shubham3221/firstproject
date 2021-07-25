package com.example.s_tools.Splash_login_reg;

import androidx.annotation.Keep;

@Keep
public class CheckCookieValid {
    private String status;
    private boolean valid;

    public CheckCookieValid(String status, boolean valid) {
        this.status=status;
        this.valid=valid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status=status;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid=valid;
    }
}
