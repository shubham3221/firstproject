package com.example.s_tools.tools.ImageShack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageStackApiCall {

    public static final String TAG="mtag";

    public interface ApiCallback {
        void onResponse(boolean success, ImagestackData modelList);
    }
    public static void getImages(ApiCallback apiCallback){
        ImageShackApi api = ImageShackApi.retrofit.create(ImageShackApi.class);
        api.getSlideCoverPhotos(10).enqueue(new Callback<ImagestackData>() {
            @Override
            public void onResponse(Call<ImagestackData> call, Response<ImagestackData> response) {
                if (response.body() != null){
                    apiCallback.onResponse(true,response.body());
                }
            }

            @Override
            public void onFailure(Call<ImagestackData> call, Throwable t) {
                apiCallback.onResponse(false,null);
            }
        });
    }
}
