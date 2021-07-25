package com.example.s_tools.premium;

import android.content.Context;

import com.example.s_tools.MyWebService;
import com.example.s_tools.premium.model.PremiumModel;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.ToastMy;
import com.example.s_tools.tools.retrofitcalls.MySharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MyMovies.CU_MA;

public class Dialing {
    public interface ApiCallback {
        void onResponse(boolean success, PremiumModel body);
    }
    public static void getData(Context context, int mpage,int count, ApiCallback apiCallback){
        MyWebService myWebService=MyWebService.retrofit.create(MyWebService.class);
        myWebService.getPremiumPost(3,count,mpage).enqueue(new Callback<PremiumModel>() {
            @Override
            public void onResponse(Call<PremiumModel> call, Response<PremiumModel> response) {
                if (response.body() != null){
                    if (MySharedPref.isVersionOut(context)){
                        ToastMy.errorToast(context,"App Update Found! Please Restart App To Continue", ToastMy.LENGTH_LONG);
                        apiCallback.onResponse(false,null);
                    }else {
                        apiCallback.onResponse(true,response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<PremiumModel> call, Throwable t) {
                apiCallback.onResponse(false,null);
                if (t.getMessage().contains(CU_MA)){
                    ToastMy.errorToast(context, Cvalues.OFFLINE, ToastMy.LENGTH_SHORT);
                }else {
                    ToastMy.errorToast(context, t.getMessage().replace(CU_MA,""), ToastMy.LENGTH_SHORT);
                }
            }
        });
    }
    public static final void updateData(Context context,int mpage,ApiCallback apiCallback){
        MyWebService myWebService=MyWebService.retrofit.create(MyWebService.class);
        myWebService.getPremiumPost(3,1,mpage).enqueue(new Callback<PremiumModel>() {
            @Override
            public void onResponse(Call<PremiumModel> call, Response<PremiumModel> response) {
                if (response.body() != null){
                    apiCallback.onResponse(true,response.body());
                }
            }

            @Override
            public void onFailure(Call<PremiumModel> call, Throwable t) {
                apiCallback.onResponse(false,null);
                if (t.getMessage().contains(CU_MA)){
                    ToastMy.errorToast(context, "Offline", ToastMy.LENGTH_SHORT);
                }else {
                    ToastMy.errorToast(context, t.getMessage().replace(CU_MA,""), ToastMy.LENGTH_SHORT);
                }
            }
        });
    }
}
