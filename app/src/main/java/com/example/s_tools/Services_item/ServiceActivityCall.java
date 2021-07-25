package com.example.s_tools.Services_item;

import android.content.Context;

import com.example.s_tools.MyWebService;
import com.example.s_tools.tools.ToastMy;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MyMovies.CU_MA;

class ServiceActivityCall {
    public static final String TAG="mtag";
    public static final String ERROR="error";
    public static final String INVALID_COOKIE="Invalid cookie";
    public static final String PLEASE_TRY_AGAIN="Please Try Again..";
    public static final String STATUS="status";
    public static final String OK="ok";
    public static final String TOKEN="token";
    public static final String SOME_ERROR_OCCURED="Some Error Occured! ";

    public interface ApiCallback {
        void onResponse(boolean success, String token);
    }

    public interface ApiCallback2 {
        void onResponse(boolean success, String token);
    }

    public static void updateUserMeta(Context context, String cookie, String value, ApiCallback apiCallback) {
        MyWebService myWebService=MyWebService.retrofit.create(MyWebService.class);
        myWebService.updateUserMetaValue(cookie, value).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    String status=response.body().get(STATUS).getAsString();
                    if (status.contains(ERROR)) {
                        apiCallback.onResponse(false, null);
                    } else if (status.contains(OK)) {

                        myWebService.getUserMeta(cookie).enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                try {
                                    String status=response.body().get(STATUS).getAsString();

                                    if (status.contains(ERROR)) {
                                        apiCallback.onResponse(false, null);
                                    } else if (status.contains(OK)) {
                                        apiCallback.onResponse(true, response.body().get(TOKEN).getAsString());
                                    }
                                } catch (Exception e) {

                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                if (t.getMessage().contains(CU_MA)){
                                    ToastMy.errorToast(context, "Check Connection.",ToastMy.LENGTH_SHORT);
                                }else {
                                    ToastMy.errorToast(context, t.getMessage().replace(CU_MA,""),ToastMy.LENGTH_SHORT);
                                }

                            }
                        });
                    }
                } catch (Exception e) {
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (t.getMessage().contains(CU_MA)){
                    ToastMy.errorToast(context, "Check Connection.",ToastMy.LENGTH_SHORT);
                }else {
                    ToastMy.errorToast(context, t.getMessage().replace(CU_MA,""),ToastMy.LENGTH_SHORT);
                }
            }
        });
    }

    public static void getUserMeta(Context context, String cookie, ApiCallback2 apiCallback) {
        MyWebService myWebService=MyWebService.retrofit.create(MyWebService.class);
        myWebService.getUserMeta(cookie).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    String status=response.body().get(STATUS).getAsString();

                    if (status.contains(ERROR)) {
                        apiCallback.onResponse(false, null);
                    } else if (status.contains(OK)) {
                        apiCallback.onResponse(true, response.body().get(TOKEN).getAsString());
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (t.getMessage().contains(CU_MA)){
                    ToastMy.errorToast(context, "Check Connection.",ToastMy.LENGTH_SHORT);
                }else {
                    ToastMy.errorToast(context, t.getMessage().replace(CU_MA,""),ToastMy.LENGTH_SHORT);
                }
            }
        });
    }
}
