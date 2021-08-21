package com.example.s_tools.Splash_login_reg.RegisterApiCall

import android.content.Context
import com.example.s_tools.MyWebService
import com.example.s_tools.tools.retrofitcalls.NonceModel
import android.widget.Toast
import com.example.s_tools.Splash_login_reg.Registration_Model
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MyMovies
import com.example.s_tools.tools.ToastMy
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

object RegisterNewAccount {
    const val HIDDENSTOOL_MA_CU = "hiddenstool.ma.cu"
    @JvmStatic
    fun registerNew(
        context: Context?,
        name: String?,
        username: String?,
        email: String?,
        password: String?,
        desc: String?,
        callback: ApiCallback
    ) {
        //getting nonce
        val myWebService = MyWebService.retrofit.create(MyWebService::class.java)
        myWebService.getnonce("user", "register")!!.enqueue(object : Callback<NonceModel?> {
            override fun onResponse(call: Call<NonceModel?>, response: Response<NonceModel?>) {
                try {
                    val body = response.body()
                    //after getting nonce
                    if (body!!.status.contains("error")) {
                        Toast.makeText(
                            context,
                            "Error Code = 2 , Contact to Admin For Query",
                            Toast.LENGTH_SHORT
                        ).show()
                        callback.onResponse(false)
                        return
                    }
                    //registeration
                    myWebService.userRegiseration(
                        name,
                        username,
                        email,
                        password,
                        desc,
                        body.nonce,
                        2,
                        "no"
                    )
                        ?.enqueue(object : Callback<Registration_Model?> {
                            override fun onResponse(
                                call: Call<Registration_Model?>,
                                response: Response<Registration_Model?>
                            ) {
                                val registrationModel = response.body()
                                if (registrationModel!!.error == null) {
                                    //success
                                    callback.onResponse(true)
                                } else {
                                    callback.onResponse(false)
                                    Toast.makeText(
                                        context,
                                        registrationModel.error,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<Registration_Model?>, t: Throwable) {
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
                                callback.onResponse(false)
                            }
                        })
                } catch (e: Exception) {
                    callback.onResponse(false)
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<NonceModel?>, t: Throwable) {
                if (t.message!!.contains(MyMovies.CU_MA)) {
                    ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT)
                } else {
                    ToastMy.errorToast(
                        context,
                        t.message!!.replace(MyMovies.CU_MA, ""),
                        ToastMy.LENGTH_SHORT
                    )
                }
                callback.onResponse(false)
            }
        })
    }

    interface ApiCallback {
        fun onResponse(success: Boolean)
    }
}