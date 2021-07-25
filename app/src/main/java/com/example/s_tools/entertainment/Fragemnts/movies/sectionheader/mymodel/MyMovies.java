package com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel;

import android.content.Context;
import android.widget.Toast;

import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.MyWebM;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.SModel;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.ToastMy;
import com.example.s_tools.tools.retrofitcalls.MySharedPref;
import com.example.s_tools.tools.retrofitcalls.VC;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyMovies {

    public static final String ERROR_CODE="Error Code: ";
    public static final String NO_RESULT_FOUND="no result found!";
    public static String CU_MA="";
    public static final String CHECK_CONNECTION="Check Connection.";

    public static void getMovies(Context context, int category, int perpage,int page, Apicall apicall){
        MyWebM m=MyWebM.retrofit.create(MyWebM.class);
        m.fetchRecent(category, perpage,page).enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if (response.body() != null) {
                    apicall.onSuccess(response.body().getPosts());
                }else {
                    Toast.makeText(context, ERROR_CODE+response.code(), Toast.LENGTH_SHORT).show();
                    apicall.onSuccess(null);
                }
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {

                if (t.getMessage().contains(CU_MA)){
                    ToastMy.errorToast(context, CHECK_CONNECTION, ToastMy.LENGTH_SHORT);
                }else {
                    getMovies(context,category,perpage,page,apicall);
                    ToastMy.errorToast(context, t.getMessage().replace(CU_MA,"Buddy"), ToastMy.LENGTH_SHORT);
                }
                apicall.onSuccess(null);
            }
        });
    }
    public static void getSearchMovies(Context context,String query, int perpage, int page, SearchApiCall apicall){
        MyWebM m=MyWebM.retrofit.create(MyWebM.class);
        m.searchResult(query, perpage,page).enqueue(new Callback<SModel>() {
            @Override
            public void onResponse(Call<SModel> call, Response<SModel> response) {
                if (response.body() != null) {
                    if (!response.body().getPosts().isEmpty()) {
                        apicall.onSuccess(response.body());
                    }else {
                        ToastMy.errorToast(context, NO_RESULT_FOUND, ToastMy.LENGTH_SHORT);
                        apicall.onSuccess(null);
                    }
                }else {
                    Toast.makeText(context, ERROR_CODE+response.code(), Toast.LENGTH_SHORT).show();
                    apicall.onSuccess(null);
                }
            }

            @Override
            public void onFailure(Call<SModel> call, Throwable t) {
                apicall.onSuccess(null);
                if (t.getMessage().contains(CU_MA)){
                    ToastMy.errorToast(context, CHECK_CONNECTION, ToastMy.LENGTH_SHORT);
                }else {
                    ToastMy.errorToast(context, t.getMessage().replace(CU_MA,""), ToastMy.LENGTH_SHORT);
                }
            }
        });
    }
    public static void getSliderImages(Context context, SliderImagesapi apicall){
        MyWebM m=MyWebM.retrofit.create(MyWebM.class);
        m.getPage(445).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    if (response.body().get("status").getAsString().equals("ok")) {
                        int asInt=response.body().get("page").getAsJsonObject().get("custom_fields").getAsJsonObject().get("version").getAsJsonArray().get(0).getAsInt();
                        if (asInt!= VC.V1){
                            MySharedPref.putVersioninfo(context,true);
                        }
                        apicall.onSuccess(response.body().get("page").getAsJsonObject().get("custom_fields").getAsJsonObject()
                                .get("sliderimages").getAsJsonArray());
                    }else {
                        apicall.onSuccess(null);
                    }
                }else {
                    ToastMy.errorToast(context, ERROR_CODE +response.code(), ToastMy.LENGTH_SHORT);
                    apicall.onSuccess(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (t.getMessage().contains(CU_MA)){
                    ToastMy.errorToast(context, CHECK_CONNECTION, ToastMy.LENGTH_SHORT);
                }else {
                    ToastMy.errorToast(context, t.getMessage().replace(CU_MA,""), ToastMy.LENGTH_SHORT);
                }
                apicall.onSuccess(null);
            }
        });
    }
    public interface Apicall{
        void onSuccess(List<MoviesPosts> moviesPosts);
    }
    public interface SearchApiCall{
        void onSuccess(SModel moviesPosts);
    }
    public interface SliderImagesapi {
        void onSuccess(JsonArray jsonElements);
    }
    public interface SliderImagesapi2 {
        void onSuccess(JsonArray jsonElements , String msg);
    }
}
