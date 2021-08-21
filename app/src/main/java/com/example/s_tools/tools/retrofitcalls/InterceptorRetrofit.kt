package com.example.s_tools.tools.retrofitcalls

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object InterceptorRetrofit {
    @JvmStatic
    val interceptor: OkHttpClient
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return OkHttpClient.Builder().addInterceptor(interceptor).build()
        }
}