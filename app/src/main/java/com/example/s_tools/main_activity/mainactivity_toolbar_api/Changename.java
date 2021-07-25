package com.example.s_tools.main_activity.mainactivity_toolbar_api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.s_tools.MyWebService;
import com.example.s_tools.tools.retrofitcalls.MySharedPref;
import com.securepreferences.SecurePreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Changename {

    public static final String TAG = "mtag";

    public interface ApiCallback {
        void onResponse(boolean success);
    }
    public static void nameChange(Context context,String name, final MySharedPref.ApiCallback callback){
        SharedPreferences sharedPreferences =new SecurePreferences(context,"@5363372399S",
                "$Systemcr.xml");
        String cookie=sharedPreferences.getString("cookie", null);
        if (cookie==null){
            Log.i(TAG, "nameChange: cookie" );
            callback.onResponse(false);
            return;
        }
        MyWebService myWebService = MyWebService.retrofit.create(MyWebService.class);
        myWebService.updateUserMeta(cookie,"first_name",name).enqueue(new Callback<ChangeNameModel>() {
            @Override
            public void onResponse(Call<ChangeNameModel> call, Response<ChangeNameModel> response) {
                ChangeNameModel body=response.body();
                if (body.getStatus().contains("error")){
                    callback.onResponse(false);
                }else {
                    callback.onResponse(true);
                }
            }

            @Override
            public void onFailure(Call<ChangeNameModel> call, Throwable t) {

            }
        });
    }
}
