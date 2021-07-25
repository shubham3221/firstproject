package com.example.s_tools.tools.retrofitcalls;

import android.content.Context;

import com.example.s_tools.MyWebService;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.ToastMy;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MyMovies.CU_MA;

public class VC {
    public static final int V1=26;
    public static final String BLOGSPOT_COM="https://buddymy.blogspot.com/";

    public interface ApiCallback {
        void onResponse(boolean success, String updateinfo);
    }

    public static void check(Context context, ApiCallback callback) {
        MyWebService webService=MyWebService.retrofit.create(MyWebService.class);
        webService.checkVersion().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.body().get("status").getAsString().equals("ok")) {
                        int asInt=response.body().getAsJsonObject("page").getAsJsonObject("custom_fields").get("version").getAsJsonArray().get(0).getAsInt();
                        if (asInt != V1) {
                            MySharedPref.putVersioninfo(context, true);
                            ToastMy.successToast(context, "App Update Found! Please Restart The App To Continue", ToastMy.LENGTH_LONG);
                        } else {
                            String movie=response.body().getAsJsonObject("page").getAsJsonObject("custom_fields").get("movieurl").getAsJsonArray().get(0).getAsString();
                            String wall1=response.body().getAsJsonObject("page").getAsJsonObject("custom_fields").get("wallpaper").getAsJsonArray().get(0).getAsString();
                            String wall2=response.body().getAsJsonObject("page").getAsJsonObject("custom_fields").get("wallpaper").getAsJsonArray().get(1).getAsString();
                            String youtubetomp3=response.body().getAsJsonObject("page").getAsJsonObject("custom_fields").get("youtubetomp3").getAsJsonArray().get(0).getAsString();
                            MySharedPref.putMovieUrl(context, movie);
                            MySharedPref.putwall1(context, wall1);
                            MySharedPref.putwall2(context, wall2);
                            MySharedPref.putyoutubemp3(context, youtubetomp3);
                            callback.onResponse(true, null);
                        }
                    }
                } catch (Exception e) {
                    ToastMy.successToast(context, Cvalues.OFFLINE, ToastMy.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (t.getMessage().contains(CU_MA)) {
                    ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT);
                } else {
                    ToastMy.errorToast(context, t.getMessage().replace(CU_MA, ""), ToastMy.LENGTH_SHORT);
                }

            }
        });
    }

    public static void check_specific(Context context, boolean video, boolean audio, boolean update, ApiCallback callback) {
        MyWebService webService=MyWebService.retrofit.create(MyWebService.class);
        try {
            webService.checkVersion().enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        if (response.body().get("status").getAsString().equals("ok")) {
                            int asInt=response.body().getAsJsonObject("page").getAsJsonObject("custom_fields").get("version").getAsJsonArray().get(0).getAsInt();
                            if (asInt != V1) {
                                ToastMy.successToast(context, "App Update Available! ", ToastMy.LENGTH_SHORT);
                                MySharedPref.putVersioninfo(context, true);
                            }
                            if (audio) {
                                String youtubetomp3=response.body().getAsJsonObject("page").getAsJsonObject("custom_fields").get("youtubetomp3").getAsJsonArray().get(0).getAsString();
                                MySharedPref.putyoutubemp3(context, youtubetomp3);
                                callback.onResponse(true, null);
                            } else if (video) {
                                String part1=response.body().getAsJsonObject("page").getAsJsonObject("custom_fields").get("youtubevideo").getAsJsonArray().get(0).getAsString().split(",")[0];
                                String part2=response.body().getAsJsonObject("page").getAsJsonObject("custom_fields").get("youtubevideo").getAsJsonArray().get(0).getAsString().split(",")[1];
                                MySharedPref.putyoutubeVideo_part1(context, part1);
                                MySharedPref.putyoutubeVideo_part2(context, part2);
                                callback.onResponse(true, null);
                            } else if (update) {
                                String update_urls=response.body().getAsJsonObject("page").getAsJsonObject("custom_fields").get("newupdateurl").getAsJsonArray().get(0).getAsString();
                                callback.onResponse(true, update_urls);

                            }
                        }
                    } catch (Exception e) {
                        callback.onResponse(false, null);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    callback.onResponse(false, null);
                    if (t.getMessage().contains(CU_MA)) {
                        ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT);
                    } else {
                        ToastMy.errorToast(context, t.getMessage().replace(CU_MA, ""), ToastMy.LENGTH_SHORT);
                    }

                }
            });

        } catch (Exception e) {
            ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT);
            callback.onResponse(false, null);
        }
    }

    public static void check_v(Context context, ApiCallback callback) {
        MyWebService webService=MyWebService.retrofit.create(MyWebService.class);
        webService.checkVersion().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("status").getAsString().equals("ok")) {
                    int asInt=response.body().getAsJsonObject("page").getAsJsonObject("custom_fields").get("version").getAsJsonArray().get(0).getAsInt();
                    if (asInt != V1) {
                        MySharedPref.putVersioninfo(context, true);
                        String update_urls=response.body().getAsJsonObject("page").getAsJsonObject("custom_fields").get("newupdateurl").getAsJsonArray().get(0).getAsString();
                        callback.onResponse(true, update_urls);
                    } else {
                        callback.onResponse(true, null);
                    }

                } else {
                    ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT);
                    callback.onResponse(false, null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onResponse(false, null);
                if (t.getMessage().contains(CU_MA)) {
                    ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT);
                } else {
                    ToastMy.errorToast(context, t.getMessage().replace(CU_MA, ""), ToastMy.LENGTH_SHORT);
                }

            }
        });
    }
}
