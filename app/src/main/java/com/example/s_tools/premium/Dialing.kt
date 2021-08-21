package com.example.s_tools.premium

import android.content.Context
import com.example.s_tools.MyWebService
import com.example.s_tools.premium.model.PremiumModel
import com.example.s_tools.tools.retrofitcalls.MySharedPref
import com.example.s_tools.tools.ToastMy
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MyMovies
import com.example.s_tools.tools.Cvalues
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object Dialing {
    @JvmStatic
    fun getData(context: Context?, mpage: Int, count: Int, apiCallback: (Boolean,PremiumModel?)->Unit) {
        val myWebService = MyWebService.retrofit.create(MyWebService::class.java)
        myWebService.getPremiumPost(3, count, mpage)!!.enqueue(object : Callback<PremiumModel?> {
            override fun onResponse(call: Call<PremiumModel?>, response: Response<PremiumModel?>) {
                if (response.body() != null) {
                    if (MySharedPref.isVersionOut(context)) {
                        ToastMy.errorToast(
                            context,
                            "App Update Found! Please Restart App To Continue",
                            ToastMy.LENGTH_LONG
                        )
                        apiCallback(false, null)
                    } else {
                        apiCallback(true, response.body())
                    }
                }
            }

            override fun onFailure(call: Call<PremiumModel?>, t: Throwable) {
                apiCallback(false, null)
                if (t.message!!.contains(MyMovies.CU_MA)) {
                    ToastMy.errorToast(context, Cvalues.OFFLINE, ToastMy.LENGTH_SHORT)
                } else {
                    ToastMy.errorToast(
                        context,
                        t.message!!.replace(MyMovies.CU_MA, ""),
                        ToastMy.LENGTH_SHORT
                    )
                }
            }
        })
    }

    fun updateData(context: Context?, mpage: Int, apiCallback: ApiCallback) {
        val myWebService = MyWebService.retrofit.create(MyWebService::class.java)
        myWebService.getPremiumPost(3, 1, mpage)!!.enqueue(object : Callback<PremiumModel?> {
            override fun onResponse(call: Call<PremiumModel?>, response: Response<PremiumModel?>) {
                if (response.body() != null) {
                    apiCallback.onResponse(true, response.body())
                }
            }

            override fun onFailure(call: Call<PremiumModel?>, t: Throwable) {
                apiCallback.onResponse(false, null)
                if (t.message!!.contains(MyMovies.CU_MA)) {
                    ToastMy.errorToast(context, "Offline", ToastMy.LENGTH_SHORT)
                } else {
                    ToastMy.errorToast(
                        context,
                        t.message!!.replace(MyMovies.CU_MA, ""),
                        ToastMy.LENGTH_SHORT
                    )
                }
            }
        })
    }

    interface ApiCallback {
        fun onResponse(success: Boolean, body: PremiumModel?)
    }
}