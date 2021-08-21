package com.example.s_tools.Splash_login_reg.loginapi_call

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import com.example.s_tools.tools.retrofitcalls.MySharedPref.putLoginData
import com.example.s_tools.Splash_login_reg.loginapi_call.LoginApiCall
import com.example.s_tools.MyWebService
import com.example.s_tools.Splash_login_reg.LoginModel
import com.example.s_tools.tools.retrofitcalls.MySharedPref
import android.widget.Toast
import com.example.s_tools.R
import android.widget.TextView
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object LoginApiCall {
    @JvmStatic
    fun login(context: Context?, email: String?, pass: String?, callback: (Boolean)->Unit) {
        val myWebService = MyWebService.retrofit.create(MyWebService::class.java)
        myWebService.loginApi(email, pass)!!.enqueue(object : Callback<LoginModel?> {
            override fun onResponse(call: Call<LoginModel?>, response: Response<LoginModel?>) {
                if (response.body()!!.error == null) {
                    putLoginData(
                        context, response.body()!!.userData.id,
                        response.body()!!.cookie, response.body()!!.userData.username,
                        response.body()!!.userData.firstname, response.body()!!.userData.email, pass
                    )
                    callback(true)
                } else {
                    callback(false)
                    if (response.body()!!.error.contains("password")) {
                        Toast.makeText(context, "Invalid Password", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Invalid", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<LoginModel?>, t: Throwable) {
                callback(false)
                if (t.message!!.contains("certificate")) {
                    Toast.makeText(
                        context,
                        "Server error.Please Visit buddymy.blogspot.com",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val dialog = Dialog(context!!)
                    dialog.setContentView(R.layout.movietimeout)
                    val button = dialog.findViewById<Button>(R.id.dismissbtn)
                    val textView = dialog.findViewById<TextView>(R.id.errorText)
                    textView.text = "Unable to receive data. try after some tome"
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.BLACK))
                    dialog.show()
                    button.setOnClickListener { v: View? -> dialog.dismiss() }
                }
            }
        })
    }

    interface ApiCallback {
        fun onResponse(success: Boolean)
    }
}