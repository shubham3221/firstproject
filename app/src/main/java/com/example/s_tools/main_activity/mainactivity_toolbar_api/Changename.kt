package com.example.s_tools.main_activity.mainactivity_toolbar_api

import android.content.Context
import com.example.s_tools.tools.retrofitcalls.MySharedPref
import android.content.SharedPreferences
import android.util.Log
import com.securepreferences.SecurePreferences
import com.example.s_tools.main_activity.mainactivity_toolbar_api.Changename
import com.example.s_tools.MyWebService
import com.example.s_tools.main_activity.mainactivity_toolbar_api.ChangeNameModel
import androidx.annotation.Keep
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object Changename {
    const val TAG = "mtag"
    @JvmStatic
    fun nameChange(context: Context?, name: String?, callback: MySharedPref.ApiCallback) {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, "@5363372399S",
            "\$Systemcr.xml"
        )
        val cookie = sharedPreferences.getString("cookie", null)
        if (cookie == null) {
            Log.i(TAG, "nameChange: cookie")
            callback.onResponse(false)
            return
        }
        val myWebService = MyWebService.retrofit.create(MyWebService::class.java)
        myWebService.updateUserMeta2(cookie, "first_name", name)!!
            .enqueue(object : Callback<ChangeNameModel?> {
                override fun onResponse(
                    call: Call<ChangeNameModel?>,
                    response: Response<ChangeNameModel?>
                ) {
                    val body = response.body()
                    if (body?.status!!.contains("error")) {
                        callback.onResponse(false)
                    } else {
                        callback.onResponse(true)
                    }
                }

                override fun onFailure(call: Call<ChangeNameModel?>, t: Throwable) {}
            })
    }

    interface ApiCallback {
        fun onResponse(success: Boolean)
    }
}