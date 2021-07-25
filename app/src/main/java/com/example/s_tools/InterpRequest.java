package com.example.s_tools;

import android.content.Context;

import com.example.s_tools.tools.retrofitcalls.MySharedPref;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

public class InterpRequest {
    public OkHttpClient getHttpClient(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return  new OkHttpClient.Builder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .callTimeout(60,TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS)
//                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + MySharedPref.getTokenMessages(context))
                            .build();
                    return chain.proceed(newRequest);
                })

                .build();

    }
}
