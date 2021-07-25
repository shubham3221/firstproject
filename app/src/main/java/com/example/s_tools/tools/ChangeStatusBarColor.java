package com.example.s_tools.tools;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class ChangeStatusBarColor {
    public static void changeStatusbarColor(Activity context,int resourse) {
        Window window = context.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(context.getResources().getColor(resourse));
    }
}
