package com.example.s_tools.Services_item

import android.content.Context
import com.example.s_tools.MyWebService
import com.google.gson.JsonObject
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MyMovies
import com.example.s_tools.tools.ToastMy
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

internal object ServiceActivityCall {
    const val TAG = "mtag"
    const val ERROR = "error"
    const val INVALID_COOKIE = "Invalid cookie"
    const val PLEASE_TRY_AGAIN = "Please Try Again.."
    const val STATUS = "status"
    const val OK = "ok"
    const val TOKEN = "token"
    const val SOME_ERROR_OCCURED = "Some Error Occured! "
    fun updateUserMeta(
        context: Context?,
        cookie: String?,
        value: String?,
        apiCallback: ApiCallback
    ) {
        val myWebService = MyWebService.retrofit.create(MyWebService::class.java)
        myWebService.updateUserMetaValue(cookie, value)!!.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    val status = response.body()!![STATUS].asString
                    if (status.contains(ERROR)) {
                        apiCallback.onResponse(false, null)
                    } else if (status.contains(OK)) {
                        myWebService.getUserMetaWithCall(cookie)!!
                            .enqueue(object : Callback<JsonObject?> {
                                override fun onResponse(
                                    call: Call<JsonObject?>,
                                    response: Response<JsonObject?>
                                ) {
                                    try {
                                        val status = response.body()!![STATUS].asString
                                        if (status.contains(ERROR)) {
                                            apiCallback.onResponse(false, null)
                                        } else if (status.contains(OK)) {
                                            apiCallback.onResponse(
                                                true,
                                                response.body()!![TOKEN].asString
                                            )
                                        }
                                    } catch (e: Exception) {
                                    }
                                }

                                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                                    if (t.message!!.contains(MyMovies.CU_MA)) {
                                        ToastMy.errorToast(
                                            context,
                                            "Check Connection.",
                                            ToastMy.LENGTH_SHORT
                                        )
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
                } catch (e: Exception) {
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                if (t.message!!.contains(MyMovies.CU_MA)) {
                    ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT)
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

    fun getUserMeta(context: Context?, cookie: String?, apiCallback: ApiCallback2) {
        val myWebService = MyWebService.retrofit.create(MyWebService::class.java)
        myWebService.getUserMetaWithCall(cookie)!!.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    val status = response.body()!![STATUS].asString
                    if (status.contains(ERROR)) {
                        apiCallback.onResponse(false, null)
                    } else if (status.contains(OK)) {
                        apiCallback.onResponse(true, response.body()!![TOKEN].asString)
                    }
                } catch (e: Exception) {
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                if (t.message!!.contains(MyMovies.CU_MA)) {
                    ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT)
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
        fun onResponse(success: Boolean, token: String?)
    }

    interface ApiCallback2 {
        fun onResponse(success: Boolean, token: String?)
    }
}