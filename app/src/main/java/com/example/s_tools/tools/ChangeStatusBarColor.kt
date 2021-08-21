package com.example.s_tools.tools

import android.app.Activity
import android.view.WindowManager

object ChangeStatusBarColor {
    fun changeStatusbarColor(context: Activity, resourse: Int) {
        val window = context.window

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

// finally change the color
        window.statusBarColor = context.resources.getColor(resourse)
    }
}