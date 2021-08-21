package com.example.s_tools.tools.retrofitcalls

import android.content.Context
import com.example.s_tools.tools.retrofitcalls.MySharedPref.putVersioninfo
import com.example.s_tools.tools.retrofitcalls.MySharedPref.putMovieUrl
import com.example.s_tools.tools.retrofitcalls.MySharedPref.putwall1
import com.example.s_tools.tools.retrofitcalls.MySharedPref.putwall2
import com.example.s_tools.tools.retrofitcalls.MySharedPref.putyoutubemp3
import com.example.s_tools.tools.retrofitcalls.MySharedPref.putyoutubeVideo_part1
import com.example.s_tools.tools.retrofitcalls.MySharedPref.putyoutubeVideo_part2
import com.example.s_tools.MyWebService
import com.google.gson.JsonObject
import com.example.s_tools.tools.retrofitcalls.MySharedPref
import com.example.s_tools.tools.ToastMy
import com.example.s_tools.tools.Cvalues
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MyMovies
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

object VC {
    const val V1 = 26
    const val BLOGSPOT_COM = "https://buddymy.blogspot.com/"
    @JvmStatic
    fun check(context: Context?, callback: (Boolean,String?)->Unit) {
        val webService = MyWebService.retrofit.create(MyWebService::class.java)
        webService.checkVersion()!!.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    if (response.body()!!["status"].asString == "ok") {
                        val asInt = response.body()!!.getAsJsonObject("page")
                            .getAsJsonObject("custom_fields")["version"].asJsonArray[0].asInt
                        if (asInt != V1) {
                            putVersioninfo(context, true)
                            ToastMy.successToast(
                                context,
                                "App Update Found! Please Restart The App To Continue",
                                ToastMy.LENGTH_LONG
                            )
                        } else {
                            val movie = response.body()!!.getAsJsonObject("page")
                                .getAsJsonObject("custom_fields")["movieurl"].asJsonArray[0].asString
                            val wall1 = response.body()!!.getAsJsonObject("page")
                                .getAsJsonObject("custom_fields")["wallpaper"].asJsonArray[0].asString
                            val wall2 = response.body()!!.getAsJsonObject("page")
                                .getAsJsonObject("custom_fields")["wallpaper"].asJsonArray[1].asString
                            val youtubetomp3 = response.body()!!.getAsJsonObject("page")
                                .getAsJsonObject("custom_fields")["youtubetomp3"].asJsonArray[0].asString
                            putMovieUrl(context, movie)
                            putwall1(context, wall1)
                            putwall2(context, wall2)
                            putyoutubemp3(context, youtubetomp3)
                            callback(true, null)
                        }
                    }
                } catch (e: Exception) {
                    ToastMy.successToast(context, Cvalues.OFFLINE, ToastMy.LENGTH_LONG)
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

    @JvmStatic
    fun check_specific(
        context: Context?,
        video: Boolean,
        audio: Boolean,
        update: Boolean,
        callback: (Boolean,String?)->Unit
    ) {
        val webService = MyWebService.retrofit.create(MyWebService::class.java)
        try {
            webService.checkVersion()!!.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        if (response.body()!!["status"].asString == "ok") {
                            val asInt = response.body()!!.getAsJsonObject("page")
                                .getAsJsonObject("custom_fields")["version"].asJsonArray[0].asInt
                            if (asInt != V1) {
                                ToastMy.successToast(
                                    context,
                                    "App Update Available! ",
                                    ToastMy.LENGTH_SHORT
                                )
                                putVersioninfo(context, true)
                            }
                            if (audio) {
                                val youtubetomp3 = response.body()!!.getAsJsonObject("page")
                                    .getAsJsonObject("custom_fields")["youtubetomp3"].asJsonArray[0].asString
                                putyoutubemp3(context, youtubetomp3)
                                callback(true, null)
                            } else if (video) {
                                val part1 = response.body()!!.getAsJsonObject("page")
                                    .getAsJsonObject("custom_fields")["youtubevideo"].asJsonArray[0].asString.split(
                                    ","
                                ).toTypedArray()[0]
                                val part2 = response.body()!!.getAsJsonObject("page")
                                    .getAsJsonObject("custom_fields")["youtubevideo"].asJsonArray[0].asString.split(
                                    ","
                                ).toTypedArray()[1]
                                putyoutubeVideo_part1(context, part1)
                                putyoutubeVideo_part2(context, part2)
                                callback(true, null)
                            } else if (update) {
                                val update_urls = response.body()!!.getAsJsonObject("page")
                                    .getAsJsonObject("custom_fields")["newupdateurl"].asJsonArray[0].asString
                                callback(true, update_urls)
                            }
                        }
                    } catch (e: Exception) {
                        callback(false, null)
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    callback(false, null)
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
        } catch (e: Exception) {
            ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT)
            callback(false, null)
        }
    }

    @JvmStatic
    fun check_v(context: Context?, callback: (Boolean,String?)->Unit) {
        val webService = MyWebService.retrofit.create(MyWebService::class.java)
        webService.checkVersion()!!.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                if (response.body()!!["status"].asString == "ok") {
                    val asInt = response.body()!!.getAsJsonObject("page")
                        .getAsJsonObject("custom_fields")["version"].asJsonArray[0].asInt
                    if (asInt != V1) {
                        putVersioninfo(context, true)
                        val update_urls = response.body()!!.getAsJsonObject("page")
                            .getAsJsonObject("custom_fields")["newupdateurl"].asJsonArray[0].asString
                        callback(true, update_urls)
                    } else {
                        callback(true, null)
                    }
                } else {
                    ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT)
                    callback(false, null)
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                callback(false, null)
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
        fun onResponse(success: Boolean, updateinfo: String?)
    }
}