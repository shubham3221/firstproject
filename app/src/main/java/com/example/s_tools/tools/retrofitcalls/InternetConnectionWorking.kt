package com.example.s_tools.tools.retrofitcalls

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object InternetConnectionWorking {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}